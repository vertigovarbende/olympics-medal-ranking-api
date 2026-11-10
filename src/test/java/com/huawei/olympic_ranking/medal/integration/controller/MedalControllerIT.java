package com.huawei.olympic_ranking.medal.integration.controller;

import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.medal.service.MedalUseCase;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.SampleDataLoader;
import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MedalController - Entegrasyon Testleri")
class MedalControllerIT extends TestContainerConfiguration {

    private static final String PATH = "/api/medals";
    private static final long UNKNOWN_ID = 999_999L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedalUseCase medalUseCase;

    @Autowired
    private SampleDataLoader sampleData;

    private Long meteGazozId;
    private Long archeryId;

    @BeforeEach
    void loadAthletesAndSports() {
        this.sampleData.loadCountries();
        this.sampleData.loadSports();
        this.sampleData.loadAthletes();

        this.meteGazozId = this.sampleData.athleteId(SampleData.METE_GAZOZ);
        this.archeryId = this.sampleData.sportId(SampleData.ARCHERY);
    }

    @Test
    @DisplayName("POST /api/medals gecerli istekte 201 doner ve madalyanin ulkesini sporcudan turetir")
    void shouldCreateMedal_WhenValidRequest() throws Exception {

        // Given: Mete Gazoz (TUR) ve Archery kayitli, madalya yok
        final String request = medalJson(this.meteGazozId, this.archeryId, "GOLD");

        // When: POST /api/medals
        final ResultActions result = postMedal(request);

        // Then: 201 Created, madalya bilgileri ve sporcunun ulkesi
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.id").isNumber())
                .andExpect(jsonPath("$.response.athleteId").value(this.meteGazozId))
                .andExpect(jsonPath("$.response.sportId").value(this.archeryId))
                .andExpect(jsonPath("$.response.medalType").value("GOLD"))
                .andExpect(jsonPath("$.response.countryCode").value("TUR"));

    }

    @Test
    @DisplayName("POST /api/medals ayni sporcu ve spor icin farkli madalya turunu kabul eder")
    void shouldCreateMedal_WhenSameAthleteAndSportButDifferentType() throws Exception {

        // Given: Mete Gazoz'un Archery'de altin madalyasi var
        this.medalUseCase.create(this.meteGazozId, this.archeryId, MedalType.GOLD);
        final String request = medalJson(this.meteGazozId, this.archeryId, "SILVER");

        // When: gumus madalya icin POST /api/medals
        final ResultActions result = postMedal(request);

        // Then: 201 Created
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.response.medalType").value("SILVER"));

    }

    @Test
    @DisplayName("POST /api/medals ayni sporcu, spor ve tur zaten varsa 409 doner")
    void shouldReturnConflict_WhenMedalAlreadyAwarded() throws Exception {

        // Given: Mete Gazoz'un Archery'de altin madalyasi var
        this.medalUseCase.create(this.meteGazozId, this.archeryId, MedalType.GOLD);
        final String request = medalJson(this.meteGazozId, this.archeryId, "GOLD");

        // When: ayni madalya icin tekrar POST /api/medals
        final ResultActions result = postMedal(request);

        // Then: 409 Conflict ve MEDAL_ALREADY_AWARDED
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MED_004"))
                .andExpect(jsonPath("$.header").value("MEDAL_ALREADY_AWARDED"))
                .andExpect(jsonPath("$.path").value(PATH));

    }

    @Test
    @DisplayName("POST /api/medals sporcu yoksa 404 doner")
    void shouldReturnNotFound_WhenAthleteDoesNotExist() throws Exception {

        // Given: kayitli olmayan sporcu id'si
        final String request = medalJson(UNKNOWN_ID, this.archeryId, "GOLD");

        // When: POST /api/medals
        final ResultActions result = postMedal(request);

        // Then: 404 Not Found ve ATHLETE_NOT_FOUND
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ATH_003"))
                .andExpect(jsonPath("$.header").value("ATHLETE_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value(PATH));

    }

    @Test
    @DisplayName("POST /api/medals spor yoksa 404 doner")
    void shouldReturnNotFound_WhenSportDoesNotExist() throws Exception {

        // Given: kayitli olmayan spor id'si
        final String request = medalJson(this.meteGazozId, UNKNOWN_ID, "GOLD");

        // When: POST /api/medals
        final ResultActions result = postMedal(request);

        // Then: 404 Not Found ve SPORT_NOT_FOUND
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SPR_003"))
                .andExpect(jsonPath("$.header").value("SPORT_NOT_FOUND"));

    }

    @Test
    @DisplayName("POST /api/medals govde bossa 400 doner ve uc alani da bildirir")
    void shouldReturnBadRequest_WhenBodyHasNoFields() throws Exception {

        // Given: alansiz JSON govdesi
        final String request = "{}";

        // When: POST /api/medals
        final ResultActions result = postMedal(request);

        // Then: 400 Bad Request, athleteId, sportId ve medalType alanlari
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors[?(@.field == 'athleteId')]").exists())
                .andExpect(jsonPath("$.subErrors[?(@.field == 'sportId')]").exists())
                .andExpect(jsonPath("$.subErrors[?(@.field == 'medalType')]").exists());

    }

    @Test
    @DisplayName("POST /api/medals sporcu id'si pozitif degilse 400 doner")
    void shouldReturnBadRequest_WhenAthleteIdIsNotPositive() throws Exception {

        // Given: athleteId = 0
        final String request = medalJson(0L, this.archeryId, "GOLD");

        // When: POST /api/medals
        final ResultActions result = postMedal(request);

        // Then: 400 Bad Request ve "athleteId" alani
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors[0].field").value("athleteId"));

    }

    @Test
    @DisplayName("POST /api/medals gecersiz madalya turunde 400 doner")
    void shouldReturnBadRequest_WhenMedalTypeIsInvalid() throws Exception {

        // Given: tanimsiz madalya turu
        final String request = medalJson(this.meteGazozId, this.archeryId, "PLATINUM");

        // When: POST /api/medals
        final ResultActions result = postMedal(request);

        // Then: 400 Bad Request ve MALFORMED_REQUEST
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_003"));

    }

    private ResultActions postMedal(final String body) throws Exception {
        return this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private static String medalJson(final Long athleteId, final Long sportId, final String medalType) {
        return """
                {"athleteId": %d, "sportId": %d, "medalType": "%s"}
                """.formatted(athleteId, sportId, medalType);
    }

}
