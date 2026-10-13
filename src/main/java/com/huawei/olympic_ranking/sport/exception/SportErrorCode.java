package com.huawei.olympic_ranking.sport.exception;

import com.huawei.olympic_ranking.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum SportErrorCode implements ErrorCode {

    SPORT_NAME_REQUIRED(
            "SPR_001",
            "SPORT_NAME_REQUIRED",
            "Sport name is required."
    ),

    SPORT_NAME_ALREADY_EXISTS(
            "SPR_002",
            "SPORT_NAME_ALREADY_EXISTS",
            "Sport name already exists."
    ),

    SPORT_NOT_FOUND(
            "SPR_003",
            "SPORT_NOT_FOUND",
            "Sport not found."
    ),

    SPORT_NAME_TOO_LONG(
            "SPR_004",
            "SPORT_NAME_TOO_LONG",
            "Sport name must be at most 100 characters."
    );

    private final String code;
    private final String header;
    private final String defaultMessage;

}
