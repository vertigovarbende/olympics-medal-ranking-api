package com.huawei.olympic_ranking.sport.exception;

import com.huawei.olympic_ranking.common.exception.OlympicRankingResourceNotFoundException;

import java.io.Serial;

public class SportNotFoundException extends OlympicRankingResourceNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SportNotFoundException(final Long id) {
        super(SportErrorCode.SPORT_NOT_FOUND, "Sport not found with id: " + id);
    }

}
