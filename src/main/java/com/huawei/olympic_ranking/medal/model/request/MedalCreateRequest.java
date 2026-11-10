package com.huawei.olympic_ranking.medal.model.request;

import com.huawei.olympic_ranking.medal.model.MedalType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Madalya kaydetme isteği; madalyanın ülkesi sporcudan türetilir")
public record MedalCreateRequest(

        @Schema(description = "Madalyayı kazanan sporcunun kimliği", example = "1", minimum = "1")
        @NotNull(message = "athleteId is required")
        @Positive(message = "athleteId must be positive")
        Long athleteId,

        @Schema(description = "Madalyanın kazanıldığı spor dalının kimliği", example = "1", minimum = "1")
        @NotNull(message = "sportId is required")
        @Positive(message = "sportId must be positive")
        Long sportId,

        @Schema(description = "Madalya türü", example = "GOLD")
        @NotNull(message = "medalType is required")
        MedalType medalType

) {

}
