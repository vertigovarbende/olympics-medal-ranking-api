package com.huawei.olympic_ranking.country.model.request;

import jakarta.validation.constraints.NotBlank;

public record CountryCreateRequest(

        @NotBlank(message = "name is required")
        String name,

        @NotBlank(message = "code is required")
        String code

) {

}
