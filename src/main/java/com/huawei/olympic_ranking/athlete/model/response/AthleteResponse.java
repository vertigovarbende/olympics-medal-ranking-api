package com.huawei.olympic_ranking.athlete.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kaydedilen sporcu")
public record AthleteResponse(

        @Schema(description = "Sporcu kimliği", example = "1")
        Long id,

        @Schema(description = "Sporcu adı", example = "Mete Gazoz")
        String name,

        @Schema(description = "Sporcunun temsil ettiği ülkenin kodu", example = "TUR")
        String countryCode

) {

}
