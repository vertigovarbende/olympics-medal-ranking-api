package com.huawei.olympic_ranking.sport.integration.controller;

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

@DisplayName("SportController - Entegrasyon Testleri")
class SportControllerIT extends TestContainerConfiguration {

    private static final String PATH = "/api/sports";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SampleDataLoader sampleData;

    @Test
    @DisplayName("POST /api/sports gecerli istekte 201 doner ve spor'u olusturur")
    void shouldCreateSport_WhenValidRequest() throws Exception {

        // Given: bos veritabani ve gecerli spor istegi
        final String request = sportJson(SampleData.ARCHERY);

        // When: POST /api/sports
        final ResultActions result = postSport(request);

        // Then: 201 Created ve olusan spor
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.id").isNumber())
                .andExpect(jsonPath("$.response.name").value(SampleData.ARCHERY));

    }

    @Test
    @DisplayName("POST /api/sports ayni ad varsa 409 doner")
    void shouldReturnConflict_WhenNameAlreadyExists() throws Exception {

        // Given: ornek sporlar (Archery, Swimming, Judo) zaten kayitli
        this.sampleData.loadSports();
        final String request = sportJson(SampleData.ARCHERY);

        // When: ayni adla POST /api/sports
        final ResultActions result = postSport(request);

        // Then: 409 Conflict ve SPORT_NAME_ALREADY_EXISTS
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("SPR_002"))
                .andExpect(jsonPath("$.header").value("SPORT_NAME_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.path").value(PATH));

    }

    @Test
    @DisplayName("POST /api/sports ayni ad yalnizca harf buyuklugunde farkliysa 409 doner")
    void shouldReturnConflict_WhenNameDiffersOnlyByCase() throws Exception {

        // Given: ornek sporlar (Archery, Swimming, Judo) zaten kayitli
        this.sampleData.loadSports();
        final String request = sportJson("archery");

        // When: farkli harf buyuklugunde POST /api/sports
        final ResultActions result = postSport(request);

        // Then: 409 Conflict ve SPORT_NAME_ALREADY_EXISTS
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("SPR_002"))
                .andExpect(jsonPath("$.header").value("SPORT_NAME_ALREADY_EXISTS"));

    }

    @Test
    @DisplayName("POST /api/sports ad bossa 400 doner ve alan hatasini bildirir")
    void shouldReturnBadRequest_WhenNameIsBlank() throws Exception {

        // Given: adi bos istek
        final String request = sportJson(" ");

        // When: POST /api/sports
        final ResultActions result = postSport(request);

        // Then: 400 Bad Request, VALIDATION_ERROR ve "name" alani
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors.length()").value(1))
                .andExpect(jsonPath("$.subErrors[0].field").value("name"));

    }

    @Test
    @DisplayName("POST /api/sports ad 100 karakterden uzunsa 400 doner")
    void shouldReturnBadRequest_WhenNameIsTooLong() throws Exception {

        // Given: 101 karakterlik ad
        final String request = sportJson("A".repeat(101));

        // When: POST /api/sports
        final ResultActions result = postSport(request);

        // Then: 400 Bad Request ve SPORT_NAME_TOO_LONG (veritabani hatasi degil)
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("SPR_004"))
                .andExpect(jsonPath("$.header").value("SPORT_NAME_TOO_LONG"));

    }

    @Test
    @DisplayName("POST /api/sports govde bossa 400 doner")
    void shouldReturnBadRequest_WhenBodyHasNoFields() throws Exception {

        // Given: alansiz JSON govdesi
        final String request = "{}";

        // When: POST /api/sports
        final ResultActions result = postSport(request);

        // Then: 400 Bad Request ve "name" alani
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors[?(@.field == 'name')]").exists());

    }

    private ResultActions postSport(final String body) throws Exception {
        return this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private static String sportJson(final String name) {
        return """
                {"name": "%s"}
                """.formatted(name);
    }

}
