package com.huawei.olympic_ranking.common.exception;

import java.io.Serial;

public abstract class OlympicRankingResourceNotFoundException extends OlympicRankingException {

    @Serial
    private static final long serialVersionUID = 1L;

    protected OlympicRankingResourceNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }

    protected OlympicRankingResourceNotFoundException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }

    protected OlympicRankingResourceNotFoundException(final ErrorCode errorCode, final String message, final Throwable cause) {
        super(errorCode, message, cause);
    }

}
