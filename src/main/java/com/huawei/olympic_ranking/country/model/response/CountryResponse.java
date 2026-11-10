package com.huawei.olympic_ranking.country.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kaydedilen ülke")
public record CountryResponse(

        @Schema(description = "Ülke kimliği", example = "1")
        Long id,

        @Schema(description = "3 harfli büyük harfli ülke kodu", example = "TUR")
        String code,

        @Schema(description = "Ülke adı", example = "Türkiye")
        String name

) {

}
