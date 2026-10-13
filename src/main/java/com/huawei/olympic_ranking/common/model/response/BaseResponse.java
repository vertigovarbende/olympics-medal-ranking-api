package com.huawei.olympic_ranking.common.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Başarılı yanıt zarfı")
public class BaseResponse<T> {

    @Schema(description = "Yanıtın üretildiği zaman", example = "2024-08-15T10:30:00")
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    @Schema(description = "Yanıtı izlemek için üretilen benzersiz kimlik (UUID)", example = "3f1c2a9e-5b7d-4c1e-9a52-0d6f8e4b7a10")
    @Builder.Default
    private String code = UUID.randomUUID().toString();

    @Schema(description = "İşlemin başarılı olup olmadığı", example = "true")
    private Boolean success;

    @Schema(description = "İşlemin sonucu; gövdesiz yanıtlarda yoktur")
    private T response;

    // ----- WITHOUT BODY -----
    public static <T> BaseResponse<T> success() {
        return BaseResponse.<T>builder()
                .success(true)
                .build();
    }

    // ----- WITH BODY -----
    public static <T> BaseResponse<T> success(final T response) {
        return BaseResponse.<T>builder()
                .success(true)
                .response(response)
                .build();
    }

}
