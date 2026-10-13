package com.huawei.olympic_ranking.sport.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kaydedilen spor dalı")
public record SportResponse(

        @Schema(description = "Spor dalı kimliği", example = "1")
        Long id,

        @Schema(description = "Spor dalı adı", example = "Archery")
        String name

) {

}
