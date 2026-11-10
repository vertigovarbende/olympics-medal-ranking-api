package com.huawei.olympic_ranking.athlete.exception;

import com.huawei.olympic_ranking.common.exception.OlympicRankingResourceNotFoundException;

import java.io.Serial;

public class AthleteNotFoundException extends OlympicRankingResourceNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AthleteNotFoundException(final Long id) {
        super(AthleteErrorCode.ATHLETE_NOT_FOUND, "Athlete not found with id: " + id);
    }

}
