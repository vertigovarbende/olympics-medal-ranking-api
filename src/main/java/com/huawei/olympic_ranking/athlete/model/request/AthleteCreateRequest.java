package com.huawei.olympic_ranking.athlete.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Sporcu oluşturma isteği")
public record AthleteCreateRequest(

        @Schema(description = "Sporcu adı (en fazla 100 karakter)", example = "Mete Gazoz", maxLength = 100)
        @NotBlank(message = "name is required")
        String name,

        @Schema(description = "Sporcunun temsil ettiği, kayıtlı ülkenin kodu; büyük/küçük harf duyarsızdır", example = "TUR", minLength = 3, maxLength = 3)
        @NotBlank(message = "countryCode is required")
        String countryCode

) {

}
