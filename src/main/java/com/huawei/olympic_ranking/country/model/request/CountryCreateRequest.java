package com.huawei.olympic_ranking.country.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Ülke oluşturma isteği")
public record CountryCreateRequest(

        @Schema(description = "Ülke adı (en fazla 100 karakter)", example = "Türkiye", maxLength = 100)
        @NotBlank(message = "name is required")
        String name,

        @Schema(description = "3 harfli ülke kodu; büyük/küçük harf duyarsızdır, büyük harfe normalize edilir", example = "TUR", minLength = 3, maxLength = 3)
        @NotBlank(message = "code is required")
        String code

) {

}
