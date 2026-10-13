package com.huawei.olympic_ranking.athlete.model.request;

import jakarta.validation.constraints.NotBlank;

public record AthleteCreateRequest(

        @NotBlank(message = "name is required")
        String name,

        @NotBlank(message = "countryCode is required")
        String countryCode

) {

}
