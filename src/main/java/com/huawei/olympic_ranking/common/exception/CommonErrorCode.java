package com.huawei.olympic_ranking.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(
            "GEN_001",
            "INTERNAL_SERVER_ERROR",
            "Unexpected server error."
    ),

    VALIDATION_ERROR(
            "GEN_002",
            "VALIDATION_ERROR",
            "Validation failed."
    ),

    MALFORMED_REQUEST(
            "GEN_003",
            "MALFORMED_REQUEST",
            "Request body is missing or malformed."
    ),

    RESOURCE_NOT_FOUND("GEN_005",
            "RESOURCE_NOT_FOUND",
            "The requested resource could not be found."
    ),

    METHOD_NOT_ALLOWED(
            "GEN_006",
            "METHOD_NOT_ALLOWED",
            "This HTTP method is not supported for this endpoint."
    ),

    DATA_CONFLICT(
            "GEN_007",
            "DATA_CONFLICT",
            "The request conflicts with existing data."
    ),

    UNSUPPORTED_MEDIA_TYPE(
            "GEN_008",
            "UNSUPPORTED_MEDIA_TYPE",
            "Content type is not supported; use application/json."
    );

    private final String code;
    private final String header;
    private final String defaultMessage;

}
