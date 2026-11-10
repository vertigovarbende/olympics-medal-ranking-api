package com.huawei.olympic_ranking.country.exception;

import com.huawei.olympic_ranking.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum CountryErrorCode implements ErrorCode {

    INVALID_COUNTRY_CODE(
            "CTR_001",
            "INVALID_COUNTRY_CODE",
            "Country code must be exactly 3 letters, e.g. TUR."
    ),

    COUNTRY_NAME_REQUIRED(
            "CTR_002",
            "COUNTRY_NAME_REQUIRED",
            "Country name is required."
    ),

    COUNTRY_CODE_REQUIRED(
            "CTR_003",
            "COUNTRY_CODE_REQUIRED",
            "Country code is required."
    ),

    COUNTRY_CODE_ALREADY_EXISTS(
            "CTR_004",
            "COUNTRY_CODE_ALREADY_EXISTS",
            "Country code already exists."
    ),

    COUNTRY_NOT_FOUND(
            "CTR_005",
            "COUNTRY_NOT_FOUND",
            "Country not found."
    ),

    COUNTRY_NAME_TOO_LONG(
            "CTR_006",
            "COUNTRY_NAME_TOO_LONG",
            "Country name must be at most 100 characters."
    );

    private final String code;
    private final String header;
    private final String defaultMessage;

}
