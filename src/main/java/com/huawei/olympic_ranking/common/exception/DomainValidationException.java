package com.huawei.olympic_ranking.common.exception;

import java.io.Serial;

public class DomainValidationException extends OlympicRankingValidationException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DomainValidationException(final ErrorCode errorCode) {
        super(errorCode);
    }

}
