package com.huawei.olympic_ranking.common.integration.exception.handler;

import ch.qos.logback.classic.Level;
import com.huawei.olympic_ranking.common.testsupport.ErrorProbeController;
import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ErrorProbeController.class)
@DisplayName("GlobalExceptionHandler - Entegrasyon Testleri")
class GlobalExceptionHandlerIT extends TestContainerConfiguration {

    private static final String PROBE = ErrorProbeController.PATH;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Tanimsiz yol 404 ve RESOURCE_NOT_FOUND doner")
    void shouldReturnNotFound_WhenPathDoesNotExist() throws Exception {

        // Given: uygulamada tanimli olmayan bir yol
        final String path = "/api/unknown";

        // When: GET /api/unknown
        final ResultActions result = this.mockMvc.perform(get(path));

        // Then: 404 Not Found ve GEN_005
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("GEN_005"))
                .andExpect(jsonPath("$.header").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value(path));

    }

    @Test
    @DisplayName("Desteklenmeyen HTTP metodu 405 ve METHOD_NOT_ALLOWED doner")
    void shouldReturnMethodNotAllowed_WhenHttpMethodIsNotSupported() throws Exception {

        // Given: yalnizca POST kabul eden uc nokta
        final String path = "/api/countries";

        // When: PUT /api/countries
        final ResultActions result = this.mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        // Then: 405 Method Not Allowed ve GEN_006
        result.andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("GEN_006"))
                .andExpect(jsonPath("$.header").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.path").value(path));

    }

    @Test
    @DisplayName("Yalnizca GET kabul eden siralama ucu POST'ta 405 doner")
    void shouldReturnMethodNotAllowed_WhenPostingToReadOnlyEndpoint() throws Exception {

        // Given: yalnizca GET kabul eden siralama ucu
        final String path = "/api/rankings/countries";

        // When: POST /api/rankings/countries
        final ResultActions result = this.mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        // Then: 405 Method Not Allowed ve GEN_006
        result.andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value("GEN_006"));

    }

    @Test
    @DisplayName("JSON olmayan icerik tipi 415 ve UNSUPPORTED_MEDIA_TYPE doner")
    void shouldReturnUnsupportedMediaType_WhenContentTypeIsNotJson() throws Exception {

        // Given: duz metin icerik tipiyle gonderilen istek
        final String path = "/api/countries";

        // When: text/plain ile POST /api/countries
        final ResultActions result = this.mockMvc.perform(post(path)
                .contentType(MediaType.TEXT_PLAIN)
                .content("Turkiye"));

        // Then: 415 Unsupported Media Type ve GEN_008
        result.andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("GEN_008"))
                .andExpect(jsonPath("$.header").value("UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath("$.path").value(path));

    }

    @Test
    @DisplayName("Yol degiskeni tipi uyusmazsa 400 doner ve alani bildirir")
    void shouldReturnBadRequest_WhenPathVariableTypeMismatches() throws Exception {

        // Given: Long bekleyen yol degiskenine sayi olmayan deger
        final String path = PROBE + "/typed/abc";

        // When: GET /test-errors/typed/abc
        final ResultActions result = this.mockMvc.perform(get(path));

        // Then: 400 Bad Request, VALIDATION_ERROR ve "id" alani
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.subErrors.length()").value(1))
                .andExpect(jsonPath("$.subErrors[0].field").value("id"))
                .andExpect(jsonPath("$.subErrors[0].value").value("abc"))
                .andExpect(jsonPath("$.subErrors[0].type").value("Long"));

    }

    @Test
    @DisplayName("ConstraintViolationException 400 doner ve ihlal edilen alani bildirir")
    void shouldReturnBadRequest_WhenConstraintViolationOccurs() throws Exception {

        // Given: bos adla dogrulanan nesne icin ihlal firlatan uc nokta
        final String path = PROBE + "/constraint-violation";

        // When: GET /test-errors/constraint-violation
        final ResultActions result = this.mockMvc.perform(get(path));

        // Then: 400 Bad Request, VALIDATION_ERROR ve "name" alani
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("GEN_002"))
                .andExpect(jsonPath("$.header").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.subErrors.length()").value(1))
                .andExpect(jsonPath("$.subErrors[0].field").value("name"))
                .andExpect(jsonPath("$.subErrors[0].type").value("NotBlank"));

    }

    @Test
    @DisplayName("DataIntegrityViolationException 409 doner ve ic ayrintiyi sizdirmaz")
    void shouldReturnConflict_WhenDataIntegrityViolationOccurs() throws Exception {

        // Given: veri butunlugu ihlali firlatan uc nokta
        final String path = PROBE + "/data-conflict";

        // When: GET /test-errors/data-conflict
        final ResultActions result = this.mockMvc.perform(get(path));

        // Then: 409 Conflict, DATA_CONFLICT ve genel mesaj
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("GEN_007"))
                .andExpect(jsonPath("$.header").value("DATA_CONFLICT"))
                .andExpect(jsonPath("$.message").value("The request conflicts with existing data."))
                .andExpect(content().string(not(containsString(ErrorProbeController.LEAKED_DETAIL))));

    }

    @Test
    @DisplayName("OlympicRankingException 500 doner ve kendi hata kodunu tasir")
    void shouldReturnInternalServerError_WhenDomainServerExceptionOccurs() throws Exception {

        // Given: OlympicRankingException firlatan uc nokta
        final String path = PROBE + "/server-error";

        // When: GET /test-errors/server-error
        final ResultActions result = this.mockMvc.perform(get(path));

        // Then: 500 Internal Server Error ve istisnanin kendi hata kodu
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value(ErrorProbeController.SERVER_ERROR_CODE))
                .andExpect(jsonPath("$.header").value(ErrorProbeController.SERVER_ERROR_HEADER))
                .andExpect(jsonPath("$.message").value(ErrorProbeController.SERVER_ERROR_MESSAGE));

    }

    @Test
    @DisplayName("Beklenmeyen istisna 500 doner ve ic ayrintiyi sizdirmaz")
    void shouldReturnInternalServerError_WhenUnexpectedExceptionOccurs() throws Exception {

        // Given: beklenmeyen istisna firlatan uc nokta
        final String path = PROBE + "/unexpected";

        // When: GET /test-errors/unexpected
        final ResultActions result = this.mockMvc.perform(get(path));

        // Then: 500 Internal Server Error, INTERNAL_SERVER_ERROR ve genel mesaj
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("GEN_001"))
                .andExpect(jsonPath("$.header").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("Unexpected server error."))
                .andExpect(content().string(not(containsString(ErrorProbeController.LEAKED_DETAIL))));

    }

    @Test
    @DisplayName("Hata govdesi zaman damgasi icerir, alan detayi olmayan hatada subErrors icermez")
    void shouldNotIncludeSubErrors_WhenErrorHasNoFieldDetails() throws Exception {

        // Given: alan detayi uretmeyen bir hata (405)
        final String path = "/api/countries";

        // When: PUT /api/countries
        final ResultActions result = this.mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        // Then: zaman damgasi var, subErrors yok
        result.andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.subErrors").doesNotExist());

    }

    @Test
    @DisplayName("Istemci hatasi WARN seviyesinde loglanir")
    void shouldLogWarn_WhenClientErrorOccurs() throws Exception {

        // Given: 404 uretecek tanimsiz yol
        final String path = "/api/unknown";

        // When: GET /api/unknown
        this.mockMvc.perform(get(path));

        // Then: GEN_005 kodlu WARN logu yazilir
        assertThat(this.logTracker.findMessage(Level.WARN, "responseCode:GEN_005")).isPresent();

    }

    @Test
    @DisplayName("Sunucu hatasi ERROR seviyesinde loglanir")
    void shouldLogError_WhenServerErrorOccurs() throws Exception {

        // Given: 500 uretecek uc nokta
        final String path = PROBE + "/unexpected";

        // When: GET /test-errors/unexpected
        this.mockMvc.perform(get(path));

        // Then: GEN_001 kodlu ERROR logu yazilir
        assertThat(this.logTracker.findMessage(Level.ERROR, "responseCode:GEN_001")).isPresent();

    }

}
