package com.huawei.olympic_ranking.country.integration.controller;

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

@DisplayName("CountryController - Entegrasyon Testleri")
class CountryControllerIT extends TestContainerConfiguration {

    private static final String PATH = "/api/countries";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SampleDataLoader sampleData;

    @Test
    @DisplayName("POST /api/countries gecerli istekte 201 doner ve ulkeyi olusturur")
    void shouldCreateCountry_WhenValidRequest() throws Exception {

        // Given: bos veritabani ve gecerli ulke istegi
        final String request = countryJson(SampleData.TUR_NAME, SampleData.TUR);

        // When: POST /api/countries
        final ResultActions result = postCountry(request);

        // Then: 201 Created ve olusan ulke
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.id").isNumber())
                .andExpect(jsonPath("$.response.code").value(SampleData.TUR))
                .andExpect(jsonPath("$.response.name").value(SampleData.TUR_NAME));

    }

    @Test
    @DisplayName("POST /api/countries kucuk harfli kodu buyuk harfe cevirerek kaydeder")
    void shouldNormalizeCode_WhenCodeIsLowerCase() throws Exception {

        // Given: kucuk harfli kod iceren istek
        final String request = countryJson(SampleData.USA_NAME, "usa");

        // When: POST /api/countries
        final ResultActions result = postCountry(request);

        // Then: kod buyuk harfle donulur
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.response.code").value(SampleData.USA));

    }

    @Test
    @DisplayName("POST /api/countries ayni kod varsa 409 doner")
    void shouldReturnConflict_WhenCodeAlreadyExists() throws Exception {

        // Given: ornek ulkeler (TUR, USA, JPN) zaten kayitli
        this.sampleData.loadCountries();
        final String request = countryJson("Baska Ad", "tur");

        // When: ayni kodla POST /api/countries
        final ResultActions result = postCountry(request);

        // Then: 409 Conflict ve COUNTRY_CODE_ALREADY_EXISTS
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("CTR_004"))
                .andExpect(jsonPath("$.header").value("COUNTRY_CODE_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.path").value(PATH));

    }

    @Test
    @DisplayName("POST /api/countries ad bossa 400 doner ve alan hatasini bildirir")
    void shouldReturnBadRequest_WhenNameIsBlank() throws Exception {

        // Given: adi bos istek
        final String request = countryJson(" ", SampleData.TUR);

        // When: POST /api/countries
        final ResultActions result = postCountry(request);

        // Then: 400 Bad Request, VALIDATION_ERROR ve "name" alani
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors.length()").value(1))
                .andExpect(jsonPath("$.subErrors[0].field").value("name"));

    }

    @Test
    @DisplayName("POST /api/countries govde bossa 400 doner ve iki alani da bildirir")
    void shouldReturnBadRequest_WhenBodyHasNoFields() throws Exception {

        // Given: alansiz JSON govdesi
        final String request = "{}";

        // When: POST /api/countries
        final ResultActions result = postCountry(request);

        // Then: 400 Bad Request, name ve code alanlari
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors[?(@.field == 'name')]").exists())
                .andExpect(jsonPath("$.subErrors[?(@.field == 'code')]").exists());

    }

    @Test
    @DisplayName("POST /api/countries kod 3 harf degilse 400 doner")
    void shouldReturnBadRequest_WhenCodeIsNotThreeLetters() throws Exception {

        // Given: 4 harfli kod
        final String request = countryJson(SampleData.TUR_NAME, "TURK");

        // When: POST /api/countries
        final ResultActions result = postCountry(request);

        // Then: 400 Bad Request ve INVALID_COUNTRY_CODE
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("CTR_001"))
                .andExpect(jsonPath("$.header").value("INVALID_COUNTRY_CODE"));

    }

    @Test
    @DisplayName("POST /api/countries bozuk JSON'da 400 doner")
    void shouldReturnBadRequest_WhenBodyIsMalformed() throws Exception {

        // Given: kapanmamis JSON govdesi
        final String request = "{";

        // When: POST /api/countries
        final ResultActions result = postCountry(request);

        // Then: 400 Bad Request ve MALFORMED_REQUEST
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_003"));

    }

    private ResultActions postCountry(final String body) throws Exception {
        return this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private static String countryJson(final String name, final String code) {
        return """
                {"name": "%s", "code": "%s"}
                """.formatted(name, code);
    }

}
