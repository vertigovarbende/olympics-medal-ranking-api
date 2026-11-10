package com.huawei.olympic_ranking.medal.exception;

import com.huawei.olympic_ranking.common.exception.OlympicRankingDuplicateException;
import com.huawei.olympic_ranking.medal.model.MedalType;

import java.io.Serial;

public class DuplicateMedalException extends OlympicRankingDuplicateException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateMedalException(final Long athleteId, final Long sportId, final MedalType type) {
        super(MedalErrorCode.MEDAL_ALREADY_AWARDED,
                "Medal already awarded: athleteId=" + athleteId + ", sportId=" + sportId + ", medalType=" + type);
    }

}
