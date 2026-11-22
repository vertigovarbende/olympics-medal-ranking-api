package com.huawei.olympic_ranking.common.integration.config;

import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OpenAPI Dokumantasyonu - Entegrasyon Testleri")
class OpenApiDocumentationIT extends TestContainerConfiguration {

    private static final String API_DOCS = "/v3/api-docs";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /v3/api-docs OpenAPI belgesini ve API bilgisini doner")
    void shouldReturnOpenApiDocument_WhenApiDocsRequested() throws Exception {

        // Given: calisan uygulama
        // When: GET /v3/api-docs
        final ResultActions result = this.mockMvc.perform(get(API_DOCS));

        // Then: 200 OK, OpenAPI surumu ve API basligi
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title").value("Paris 2024 Olympic Medal Ranking API"))
                .andExpect(jsonPath("$.info.version").value("1.0.0"));

    }

    @Test
    @DisplayName("GET /v3/api-docs yedi endpoint'i ve etiketlerini listeler")
    void shouldDocumentEveryEndpoint_WhenApiDocsRequested() throws Exception {

        // Given: dortu POST, ucu GET olmak uzere yedi endpoint
        // When: GET /v3/api-docs
        final ResultActions result = this.mockMvc.perform(get(API_DOCS));

        // Then: her yol ilgili yontem ve etiketle belgelenir
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/countries'].post.tags[0]").value("Countries"))
                .andExpect(jsonPath("$.paths['/api/sports'].post.tags[0]").value("Sports"))
                .andExpect(jsonPath("$.paths['/api/athletes'].post.tags[0]").value("Athletes"))
                .andExpect(jsonPath("$.paths['/api/medals'].post.tags[0]").value("Medals"))
                .andExpect(jsonPath("$.paths['/api/rankings/countries'].get.tags[0]").value("Rankings"))
                .andExpect(jsonPath("$.paths['/api/rankings/athletes'].get.tags[0]").value("Rankings"))
                .andExpect(jsonPath("$.paths['/api/rankings/sports'].get.tags[0]").value("Rankings"));

    }

    @Test
    @DisplayName("GET /v3/api-docs olusturma endpoint'lerinin basari ve hata yanitlarini belgeler")
    void shouldDocumentResponses_WhenApiDocsRequested() throws Exception {

        // Given: 201/400/404/409 donebilen olusturma endpoint'leri
        // When: GET /v3/api-docs
        final ResultActions result = this.mockMvc.perform(get(API_DOCS));

        // Then: durum kodlari ve hata govdesi semasi belgelenir
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/countries'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/api/countries'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/api/athletes'].post.responses['404']").exists())
                .andExpect(jsonPath("$.paths['/api/medals'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/api/medals'].post.responses['404']").exists())
                .andExpect(jsonPath("$.paths['/api/medals'].post.responses['409'].content['application/json'].schema").exists())
                .andExpect(jsonPath("$.paths['/api/rankings/countries'].get.responses['200']").exists())
                .andExpect(jsonPath("$.paths['/api/rankings/countries'].get.responses['400']").exists());

    }

    @Test
    @DisplayName("GET /v3/api-docs ulke siralamasinin strateji parametresini varsayilan degeriyle belgeler")
    void shouldDocumentStrategyParameter_WhenApiDocsRequested() throws Exception {

        // Given: strategy sorgu parametresi alan ulke siralamasi
        // When: GET /v3/api-docs
        final ResultActions result = this.mockMvc.perform(get(API_DOCS));

        // Then: parametre adi, konumu ve varsayilan degeri belgelenir
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/rankings/countries'].get.parameters[0].name").value("strategy"))
                .andExpect(jsonPath("$.paths['/api/rankings/countries'].get.parameters[0].in").value("query"))
                .andExpect(jsonPath("$.paths['/api/rankings/countries'].get.parameters[0].schema.default").value("OFFICIAL"));

    }

    @Test
    @DisplayName("GET /v3/api-docs istek, yanit ve hata semalarini icerir")
    void shouldDescribeSchemas_WhenApiDocsRequested() throws Exception {

        // Given: DTO'lar uzerinde @Schema aciklamalari
        // When: GET /v3/api-docs
        final ResultActions result = this.mockMvc.perform(get(API_DOCS));

        // Then: istek, siralama satiri ve hata semalari tanimlidir
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.components.schemas.MedalCreateRequest.properties.athleteId.description").exists())
                .andExpect(jsonPath("$.components.schemas.MedalCreateRequest.properties.medalType").exists())
                .andExpect(jsonPath("$.components.schemas.CountryRankingItem.properties.gold").exists())
                .andExpect(jsonPath("$.components.schemas.ErrorResponse.properties.subErrors").exists());

    }

    @Test
    @DisplayName("GET /swagger-ui/index.html Swagger arayuzunu doner")
    void shouldServeSwaggerUi_WhenUiRequested() throws Exception {

        // Given: calisan uygulama
        // When: GET /swagger-ui/index.html
        final ResultActions result = this.mockMvc.perform(get("/swagger-ui/index.html"));

        // Then: 200 OK
        result.andExpect(status().isOk());

    }

}
