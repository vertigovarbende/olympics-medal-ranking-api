package com.huawei.olympic_ranking.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI olympicRankingOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Paris 2024 Olympic Medal Ranking API")
                .version("1.0.0")
                .description("2024 Paris Olimpiyatları madalya takip API'si: ülke, spor, sporcu ve madalya kaydı; ülke, sporcu ve spor sıralamaları. "
                        + "Başarılı yanıtlar BaseResponse zarfıyla, hatalar ErrorResponse yapısıyla döner."));
    }

}
