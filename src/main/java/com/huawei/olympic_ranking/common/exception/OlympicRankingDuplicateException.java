package com.huawei.olympic_ranking.common.exception;

import java.io.Serial;

public abstract class OlympicRankingDuplicateException extends OlympicRankingException {

    @Serial
    private static final long serialVersionUID = 1L;

    protected OlympicRankingDuplicateException(final ErrorCode errorCode) {
        super(errorCode);
    }

    protected OlympicRankingDuplicateException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }

    protected OlympicRankingDuplicateException(final ErrorCode errorCode, final String message, final Throwable cause) {
        super(errorCode, message, cause);
    }

}
