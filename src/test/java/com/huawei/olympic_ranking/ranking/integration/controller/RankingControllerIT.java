package com.huawei.olympic_ranking.ranking.integration.controller;

import com.huawei.olympic_ranking.athlete.service.AthleteUseCase;
import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.medal.service.MedalUseCase;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.SampleDataLoader;
import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("RankingController - Entegrasyon Testleri")
class RankingControllerIT extends TestContainerConfiguration {

    private static final String PATH = "/api/rankings";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SampleDataLoader sampleData;

    @Autowired
    private AthleteUseCase athleteUseCase;

    @Autowired
    private MedalUseCase medalUseCase;

    @Test
    @DisplayName("GET /api/rankings/countries ornek veride TUR, USA, JPN sirasini doner")
    void shouldReturnCountriesRankedByOfficialRules_WhenSampleDataLoaded() throws Exception {

        // Given: isterdeki ornek veri (TUR 1G 1S, USA 1G 1B, JPN 1G)
        this.sampleData.loadAll();

        // When: GET /api/rankings/countries
        final ResultActions result = getRanking("countries");

        // Then: altin esit; TUR gumusle, USA bronzla one gecer
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.length()").value(3))
                .andExpect(jsonPath("$.response[0].rank").value(1))
                .andExpect(jsonPath("$.response[0].countryCode").value(SampleData.TUR))
                .andExpect(jsonPath("$.response[0].gold").value(1))
                .andExpect(jsonPath("$.response[0].silver").value(1))
                .andExpect(jsonPath("$.response[0].bronze").value(0))
                .andExpect(jsonPath("$.response[0].total").value(2))
                .andExpect(jsonPath("$.response[1].rank").value(2))
                .andExpect(jsonPath("$.response[1].countryCode").value(SampleData.USA))
                .andExpect(jsonPath("$.response[1].gold").value(1))
                .andExpect(jsonPath("$.response[1].silver").value(0))
                .andExpect(jsonPath("$.response[1].bronze").value(1))
                .andExpect(jsonPath("$.response[2].rank").value(3))
                .andExpect(jsonPath("$.response[2].countryCode").value(SampleData.JPN))
                .andExpect(jsonPath("$.response[2].total").value(1));

    }

    @Test
    @DisplayName("GET /api/rankings/countries cok altini olan ulkeyi once siralar")
    void shouldReturnCountriesRankedByGoldFirst_WhenUsaHasMoreGold() throws Exception {

        // Given: USA (2G, 1S) ve TUR (1G, 3S)
        this.sampleData.loadCountries();
        this.sampleData.loadSports();
        this.sampleData.loadAthletes();

        final Long mete = this.sampleData.athleteId(SampleData.METE_GAZOZ);
        final Long noah = this.sampleData.athleteId(SampleData.NOAH_LYLES);
        final Long abby = this.sampleData.athleteId(SampleData.ABBY_STEUTER);

        final Long archery = this.sampleData.sportId(SampleData.ARCHERY);
        final Long swimming = this.sampleData.sportId(SampleData.SWIMMING);
        final Long judo = this.sampleData.sportId(SampleData.JUDO);

        this.medalUseCase.create(noah, swimming, MedalType.GOLD);
        this.medalUseCase.create(abby, swimming, MedalType.GOLD);
        this.medalUseCase.create(noah, judo, MedalType.SILVER);
        this.medalUseCase.create(mete, archery, MedalType.GOLD);
        this.medalUseCase.create(mete, archery, MedalType.SILVER);
        this.medalUseCase.create(mete, swimming, MedalType.SILVER);
        this.medalUseCase.create(mete, judo, MedalType.SILVER);

        // When: GET /api/rankings/countries
        final ResultActions result = getRanking("countries");

        // Then: USA 1., TUR 2. (toplam madalya degil, altin sayisi belirleyici)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.response.length()").value(2))
                .andExpect(jsonPath("$.response[0].countryCode").value(SampleData.USA))
                .andExpect(jsonPath("$.response[0].gold").value(2))
                .andExpect(jsonPath("$.response[0].silver").value(1))
                .andExpect(jsonPath("$.response[1].countryCode").value(SampleData.TUR))
                .andExpect(jsonPath("$.response[1].gold").value(1))
                .andExpect(jsonPath("$.response[1].silver").value(3));

    }

    @Test
    @DisplayName("GET /api/rankings/countries madalyalar esitse ulke adina gore alfabetik siralar")
    void shouldRankByCountryName_WhenAllMedalsAreEqual() throws Exception {

        // Given: ornek veri + Japonya'ya bir gumus daha; JPN ve TUR artik 1G 1S 0B ile esit
        this.sampleData.loadAll();
        this.medalUseCase.create(this.sampleData.athleteId(SampleData.HIFUMI_ABE), this.sampleData.sportId(SampleData.JUDO), MedalType.SILVER);

        // When: GET /api/rankings/countries
        final ResultActions result = getRanking("countries");

        // Then: esitlikte "Japan" "Türkiye"'den once gelir; USA sonda
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.response[0].countryCode").value(SampleData.JPN))
                .andExpect(jsonPath("$.response[1].countryCode").value(SampleData.TUR))
                .andExpect(jsonPath("$.response[2].countryCode").value(SampleData.USA));

    }

    @Test
    @DisplayName("GET /api/rankings/athletes ornek veride Mete, Noah, Hifumi sirasini doner ve madalyasiz sporcuyu listelemez")
    void shouldReturnAthletesRankedByTotalFirst_WhenSampleDataLoaded() throws Exception {

        // Given: isterdeki ornek veri (Abby Steuter'in madalyasi yok)
        this.sampleData.loadAll();

        // When: GET /api/rankings/athletes
        final ResultActions result = getRanking("athletes");

        // Then: Mete ve Noah 2 madalya ile esit; gumus sayisi Mete'yi one gecirir
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.response.length()").value(3))
                .andExpect(jsonPath("$.response[0].rank").value(1))
                .andExpect(jsonPath("$.response[0].athleteId").value(this.sampleData.athleteId(SampleData.METE_GAZOZ)))
                .andExpect(jsonPath("$.response[0].athleteName").value(SampleData.METE_GAZOZ))
                .andExpect(jsonPath("$.response[0].total").value(2))
                .andExpect(jsonPath("$.response[0].gold").value(1))
                .andExpect(jsonPath("$.response[0].silver").value(1))
                .andExpect(jsonPath("$.response[0].bronze").value(0))
                .andExpect(jsonPath("$.response[1].rank").value(2))
                .andExpect(jsonPath("$.response[1].athleteName").value(SampleData.NOAH_LYLES))
                .andExpect(jsonPath("$.response[2].rank").value(3))
                .andExpect(jsonPath("$.response[2].athleteName").value(SampleData.HIFUMI_ABE))
                .andExpect(jsonPath("$.response[?(@.athleteName == '" + SampleData.ABBY_STEUTER + "')]").isEmpty());

    }

    @Test
    @DisplayName("GET /api/rankings/athletes kabul senaryosunda A, B, C sirasini doner")
    void shouldReturnAthletesInOrderABC_WhenAcceptanceScenarioLoaded() throws Exception {

        // Given: A (5 madalya: 2G 2S 1B), B (5: 2G 1S 2B), C (4: 3G 1S)
        this.sampleData.loadCountries();
        this.sampleData.loadSports();

        final Long archery = this.sampleData.sportId(SampleData.ARCHERY);
        final Long swimming = this.sampleData.sportId(SampleData.SWIMMING);
        final Long judo = this.sampleData.sportId(SampleData.JUDO);

        final Long a = this.athleteUseCase.create("A", SampleData.TUR).getId();
        final Long b = this.athleteUseCase.create("B", SampleData.TUR).getId();
        final Long c = this.athleteUseCase.create("C", SampleData.TUR).getId();

        award(a, archery, MedalType.GOLD, MedalType.SILVER, MedalType.BRONZE);
        award(a, swimming, MedalType.GOLD, MedalType.SILVER);

        award(b, archery, MedalType.GOLD, MedalType.SILVER);
        award(b, swimming, MedalType.GOLD, MedalType.BRONZE);
        award(b, judo, MedalType.BRONZE);

        award(c, archery, MedalType.GOLD, MedalType.SILVER);
        award(c, swimming, MedalType.GOLD);
        award(c, judo, MedalType.GOLD);

        // When: GET /api/rankings/athletes
        final ResultActions result = getRanking("athletes");

        // Then: A, B'den (gumus), B de C'den (toplam madalya) once gelir
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.response.length()").value(3))
                .andExpect(jsonPath("$.response[0].athleteName").value("A"))
                .andExpect(jsonPath("$.response[0].total").value(5))
                .andExpect(jsonPath("$.response[1].athleteName").value("B"))
                .andExpect(jsonPath("$.response[1].total").value(5))
                .andExpect(jsonPath("$.response[2].athleteName").value("C"))
                .andExpect(jsonPath("$.response[2].total").value(4))
                .andExpect(jsonPath("$.response[2].gold").value(3));

    }

    @Test
    @DisplayName("GET /api/rankings/sports ornek veride Archery, Swimming, Judo sirasini doner")
    void shouldReturnSportsRankedByTotalThenName_WhenSampleDataLoaded() throws Exception {

        // Given: isterdeki ornek veri (Archery 2, Swimming 2, Judo 1 madalya)
        this.sampleData.loadAll();

        // When: GET /api/rankings/sports
        final ResultActions result = getRanking("sports");

        // Then: toplam esit olan Archery ve Swimming ada gore siralanir, Judo sonda
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.response.length()").value(3))
                .andExpect(jsonPath("$.response[0].rank").value(1))
                .andExpect(jsonPath("$.response[0].sportId").value(this.sampleData.sportId(SampleData.ARCHERY)))
                .andExpect(jsonPath("$.response[0].sportName").value(SampleData.ARCHERY))
                .andExpect(jsonPath("$.response[0].total").value(2))
                .andExpect(jsonPath("$.response[1].sportName").value(SampleData.SWIMMING))
                .andExpect(jsonPath("$.response[1].total").value(2))
                .andExpect(jsonPath("$.response[2].rank").value(3))
                .andExpect(jsonPath("$.response[2].sportName").value(SampleData.JUDO))
                .andExpect(jsonPath("$.response[2].total").value(1));

    }

    @ParameterizedTest
    @ValueSource(strings = {"countries", "athletes", "sports"})
    @DisplayName("GET /api/rankings/{tur} madalya yoksa kayitli olsalar bile bos liste doner")
    void shouldReturnEmptyList_WhenNoMedalsAwarded(final String type) throws Exception {

        // Given: ornek ulkeler, sporlar ve sporcular kayitli ama hic madalya verilmemis
        this.sampleData.loadCountries();
        this.sampleData.loadSports();
        this.sampleData.loadAthletes();

        // When: GET /api/rankings/{tur}
        final ResultActions result = getRanking(type);

        // Then: 200 OK ve bos liste (madalyasizlar listelenmez)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isEmpty());

    }

    private ResultActions getRanking(final String type) throws Exception {
        return this.mockMvc.perform(get(PATH + "/" + type));
    }

    private void award(final Long athleteId, final Long sportId, final MedalType... types) {
        for (final MedalType type : types) {
            this.medalUseCase.create(athleteId, sportId, type);
        }
    }

}
