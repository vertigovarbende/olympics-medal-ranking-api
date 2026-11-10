package com.huawei.olympic_ranking.common.exception;

import java.io.Serial;

public abstract class OlympicRankingValidationException extends OlympicRankingException {

    @Serial
    private static final long serialVersionUID = 1L;

    protected OlympicRankingValidationException(final ErrorCode errorCode) {
        super(errorCode);
    }

    protected OlympicRankingValidationException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }

    protected OlympicRankingValidationException(final ErrorCode errorCode, final String message, final Throwable cause) {
        super(errorCode, message, cause);
    }

}
