package com.huawei.olympic_ranking.athlete.exception;

import com.huawei.olympic_ranking.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum AthleteErrorCode implements ErrorCode {

    ATHLETE_NAME_REQUIRED(
            "ATH_001",
            "ATHLETE_NAME_REQUIRED",
            "Athlete name is required."
    ),
    ATHLETE_COUNTRY_REQUIRED(
            "ATH_002",
            "ATHLETE_COUNTRY_REQUIRED",
            "Athlete must represent a country."
    ),
    ATHLETE_NOT_FOUND(
            "ATH_003",
            "ATHLETE_NOT_FOUND",
            "Athlete not found."
    ),
    ATHLETE_NAME_TOO_LONG(
            "ATH_004",
            "ATHLETE_NAME_TOO_LONG",
            "Athlete name must be at most 100 characters."
    );

    private final String code;
    private final String header;
    private final String defaultMessage;

}
