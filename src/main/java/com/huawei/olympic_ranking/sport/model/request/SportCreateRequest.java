package com.huawei.olympic_ranking.sport.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Spor dalı oluşturma isteği")
public record SportCreateRequest(

        @Schema(description = "Spor dalı adı (en fazla 100 karakter); tekrar kontrolü büyük/küçük harf duyarsızdır", example = "Archery", maxLength = 100)
        @NotBlank(message = "name is required")
        String name

) {

}
