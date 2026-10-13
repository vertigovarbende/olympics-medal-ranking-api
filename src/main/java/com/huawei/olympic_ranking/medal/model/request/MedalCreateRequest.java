package com.huawei.olympic_ranking.medal.model.request;

import com.huawei.olympic_ranking.medal.model.MedalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MedalCreateRequest(

        @NotNull(message = "athleteId is required")
        @Positive(message = "athleteId must be positive")
        Long athleteId,

        @NotNull(message = "sportId is required")
        @Positive(message = "sportId must be positive")
        Long sportId,

        @NotNull(message = "medalType is required")
        MedalType medalType

) {

}
