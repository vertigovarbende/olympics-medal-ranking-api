package com.huawei.olympic_ranking.sport.model.request;

import jakarta.validation.constraints.NotBlank;

public record SportCreateRequest(

        @NotBlank(message = "name is required")
        String name

) {

}
