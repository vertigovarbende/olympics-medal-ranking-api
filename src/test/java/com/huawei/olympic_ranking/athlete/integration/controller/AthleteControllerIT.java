package com.huawei.olympic_ranking.athlete.integration.controller;

import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.SampleDataLoader;
import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AthleteController - Entegrasyon Testleri")
class AthleteControllerIT extends TestContainerConfiguration {

    private static final String PATH = "/api/athletes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SampleDataLoader sampleData;

    @Test
    @DisplayName("POST /api/athletes gecerli istekte 201 doner ve sporcuyu ulkesiyle olusturur")
    void shouldCreateAthlete_WhenValidRequest() throws Exception {

        // Given: ornek ulkeler kayitli ve gecerli sporcu istegi
        this.sampleData.loadCountries();
        final String request = athleteJson(SampleData.METE_GAZOZ, SampleData.TUR);

        // When: POST /api/athletes
        final ResultActions result = postAthlete(request);

        // Then: 201 Created ve olusan sporcu
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.id").isNumber())
                .andExpect(jsonPath("$.response.name").value(SampleData.METE_GAZOZ))
                .andExpect(jsonPath("$.response.countryCode").value(SampleData.TUR));

    }

    @Test
    @DisplayName("POST /api/athletes kucuk harfli ulke kodunu da ayni ulkeyle eslestirir")
    void shouldResolveCountry_WhenCountryCodeIsLowerCase() throws Exception {

        // Given: ornek ulkeler kayitli, istek kucuk harfli kod iceriyor
        this.sampleData.loadCountries();
        final String request = athleteJson(SampleData.METE_GAZOZ, "tur");

        // When: POST /api/athletes
        final ResultActions result = postAthlete(request);

        // Then: 201 Created ve ulke kodu buyuk harfle donulur
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.response.countryCode").value(SampleData.TUR));

    }

    @Test
    @DisplayName("POST /api/athletes ayni adli ikinci sporcuyu da kabul eder")
    void shouldCreateAthlete_WhenSameNameAlreadyExists() throws Exception {

        // Given: ornek sporcular (Mete Gazoz dahil) kayitli; sporcu adi benzersiz degildir
        this.sampleData.loadCountries();
        this.sampleData.loadAthletes();
        final String request = athleteJson(SampleData.METE_GAZOZ, SampleData.TUR);

        // When: POST /api/athletes
        final ResultActions result = postAthlete(request);

        // Then: 201 Created
        result.andExpect(status().isCreated());

    }

    @Test
    @DisplayName("POST /api/athletes ulke yoksa 404 doner")
    void shouldReturnNotFound_WhenCountryDoesNotExist() throws Exception {

        // Given: kayitli ulke yok
        final String request = athleteJson(SampleData.METE_GAZOZ, SampleData.TUR);

        // When: POST /api/athletes
        final ResultActions result = postAthlete(request);

        // Then: 404 Not Found ve COUNTRY_NOT_FOUND
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("CTR_005"))
                .andExpect(jsonPath("$.header").value("COUNTRY_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value(PATH));

    }

    @Test
    @DisplayName("POST /api/athletes ad bossa 400 doner ve alan hatasini bildirir")
    void shouldReturnBadRequest_WhenNameIsBlank() throws Exception {

        // Given: adi bos istek
        this.sampleData.loadCountries();
        final String request = athleteJson(" ", SampleData.TUR);

        // When: POST /api/athletes
        final ResultActions result = postAthlete(request);

        // Then: 400 Bad Request, VALIDATION_ERROR ve "name" alani
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors.length()").value(1))
                .andExpect(jsonPath("$.subErrors[0].field").value("name"));

    }

    @Test
    @DisplayName("POST /api/athletes govde bossa 400 doner ve iki alani da bildirir")
    void shouldReturnBadRequest_WhenBodyHasNoFields() throws Exception {

        // Given: alansiz JSON govdesi
        final String request = "{}";

        // When: POST /api/athletes
        final ResultActions result = postAthlete(request);

        // Then: 400 Bad Request, name ve countryCode alanlari
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors[?(@.field == 'name')]").exists())
                .andExpect(jsonPath("$.subErrors[?(@.field == 'countryCode')]").exists());

    }

    @Test
    @DisplayName("POST /api/athletes ad 100 karakterden uzunsa 400 doner")
    void shouldReturnBadRequest_WhenNameIsTooLong() throws Exception {

        // Given: ulke kayitli ve 101 karakterlik sporcu adi
        this.sampleData.loadCountries();
        final String request = athleteJson("A".repeat(101), SampleData.TUR);

        // When: POST /api/athletes
        final ResultActions result = postAthlete(request);

        // Then: 400 Bad Request ve ATHLETE_NAME_TOO_LONG (veritabani hatasi degil)
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ATH_004"))
                .andExpect(jsonPath("$.header").value("ATHLETE_NAME_TOO_LONG"));

    }

    private ResultActions postAthlete(final String body) throws Exception {
        return this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private static String athleteJson(final String name, final String countryCode) {
        return """
                {"name": "%s", "countryCode": "%s"}
                """.formatted(name, countryCode);
    }

}
