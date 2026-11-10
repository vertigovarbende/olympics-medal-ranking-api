package com.huawei.olympic_ranking.medal.model.response;

import com.huawei.olympic_ranking.medal.model.MedalType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kaydedilen madalya")
public record MedalResponse(

        @Schema(description = "Madalya kimliği", example = "1")
        Long id,

        @Schema(description = "Madalyayı kazanan sporcunun kimliği", example = "1")
        Long athleteId,

        @Schema(description = "Madalyanın kazanıldığı spor dalının kimliği", example = "1")
        Long sportId,

        @Schema(description = "Madalya türü", example = "GOLD")
        MedalType medalType,

        @Schema(description = "Madalyayı alan ülkenin kodu (sporcunun ülkesi)", example = "TUR")
        String countryCode

) {

}
