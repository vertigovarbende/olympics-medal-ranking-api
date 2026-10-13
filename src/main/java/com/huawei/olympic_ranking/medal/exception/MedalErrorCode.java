package com.huawei.olympic_ranking.medal.exception;

import com.huawei.olympic_ranking.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum MedalErrorCode implements ErrorCode {

    MEDAL_ATHLETE_REQUIRED(
            "MED_001",
            "MEDAL_ATHLETE_REQUIRED",
            "A medal must be awarded to an athlete."
    ),

    MEDAL_SPORT_REQUIRED(
            "MED_002",
            "MEDAL_SPORT_REQUIRED",
            "A medal must be awarded in a sport."
    ),
    MEDAL_TYPE_REQUIRED(
            "MED_003",
            "MEDAL_TYPE_REQUIRED",
            "Medal type is required (GOLD, SILVER or BRONZE)."
    ),

    MEDAL_ALREADY_AWARDED(
            "MED_004",
            "MEDAL_ALREADY_AWARDED",
            "This medal has already been awarded to the athlete in this sport."
    );

    private final String code;
    private final String header;
    private final String defaultMessage;

}
