package com.huawei.olympic_ranking.sport.exception;

import com.huawei.olympic_ranking.common.exception.OlympicRankingDuplicateException;

import java.io.Serial;

public class DuplicateSportException extends OlympicRankingDuplicateException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateSportException(final String name) {
        super(SportErrorCode.SPORT_NAME_ALREADY_EXISTS, "Sport already exists with name: " + name);
    }

}
