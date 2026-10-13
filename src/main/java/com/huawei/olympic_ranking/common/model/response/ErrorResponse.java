package com.huawei.olympic_ranking.common.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Builder
@Schema(description = "Hata yanıtı")
public class ErrorResponse {

    @Schema(description = "Hatanın üretildiği zaman", example = "2024-08-15T10:30:00")
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    @Schema(description = "Makine tarafından okunabilir hata kodu", example = "ATH_003")
    private String code;

    @Schema(description = "Hata başlığı", example = "ATHLETE_NOT_FOUND")
    private String header;

    @Schema(description = "İnsan tarafından okunabilir hata mesajı", example = "Athlete not found with id: 99")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @Schema(description = "Hata yanıtlarında her zaman false", example = "false")
    @Builder.Default
    private final Boolean isSuccess = false;

    @Schema(description = "İsteğin yolu", example = "/api/medals")
    private String path;

    @Schema(description = "Alan bazlı doğrulama ayrıntıları; yalnızca doğrulama hatalarında bulunur")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubError> subErrors;

    @Getter
    @Builder
    @Schema(description = "Alan bazlı hata ayrıntısı")
    public static class SubError {

        @Schema(description = "Alana ait hata mesajı", example = "name is required")
        private String message;

        @Schema(description = "Hatalı alanın adı", example = "name")
        private String field;

        @Schema(description = "Reddedilen değer", example = " ")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object value;

        @Schema(description = "İhlal edilen kısıt veya beklenen tip", example = "NotBlank")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String type;

    }

    // ----- MethodArgumentNotValidException ------
    public static ErrorResponse.ErrorResponseBuilder subErrors(final List<FieldError> fieldErrors) {

        if (CollectionUtils.isEmpty(fieldErrors)) {
            return ErrorResponse.builder();
        }

        final List<SubError> subErrorErrors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {

            final SubError.SubErrorBuilder subErrorBuilder = SubError.builder();
            List<String> codes = List.of(Objects.requireNonNull(fieldError.getCodes()));

            if (!codes.isEmpty()) {
                subErrorBuilder.field(StringUtils.substringAfterLast(codes.get(0), "."));
                if (!"AssertTrue".equals(codes.get(codes.size() - 1))) {
                    subErrorBuilder.type(StringUtils.substringAfterLast(codes.get(codes.size() - 2), ".").replace('$', '.'));
                }
            }

            if (fieldError.getRejectedValue() != null) {
                subErrorBuilder.value(fieldError.getRejectedValue().toString());
            }

            subErrorBuilder.message(fieldError.getDefaultMessage());
            subErrorErrors.add(subErrorBuilder.build());
        }

        return ErrorResponse.builder().subErrors(subErrorErrors);
    }

    // ----- ConstraintViolationException ------
    public static ErrorResponse.ErrorResponseBuilder subErrors(final Set<ConstraintViolation<?>> constraintViolations) {

        if (CollectionUtils.isEmpty(constraintViolations)) {
            return ErrorResponse.builder();
        }

        final List<SubError> subErrors = new ArrayList<>();

        constraintViolations.forEach(constraintViolation -> {

                    final String propertyPath = constraintViolation.getPropertyPath().toString();

                    final SubError.SubErrorBuilder subErrorBuilder = SubError.builder()
                            .message(constraintViolation.getMessage())
                            .field(propertyPath.contains(".") ? StringUtils.substringAfterLast(propertyPath, ".") : propertyPath)
                            .type(constraintViolation.getConstraintDescriptor()
                                    .getAnnotation()
                                    .annotationType()
                                    .getSimpleName());

                    if (constraintViolation.getInvalidValue() != null) {
                        subErrorBuilder.value(constraintViolation.getInvalidValue().toString());
                    }

                    subErrors.add(subErrorBuilder.build());
                }
        );

        return ErrorResponse.builder().subErrors(subErrors);
    }

    // ----- MethodArgumentTypeMismatchException ------
    public static ErrorResponse.ErrorResponseBuilder subErrors(final MethodArgumentTypeMismatchException exception) {

        final SubError.SubErrorBuilder subErrorBuilder = SubError.builder()
                .field(exception.getName())
                .message(String.format("Invalid value for field '%s'.", exception.getName()));

        if (exception.getValue() != null) {
            subErrorBuilder.value(exception.getValue().toString());
        }

        if (exception.getRequiredType() != null) {
            subErrorBuilder.type(exception.getRequiredType().getSimpleName());
        }

        return ErrorResponse.builder().subErrors(List.of(subErrorBuilder.build()));
    }

}
