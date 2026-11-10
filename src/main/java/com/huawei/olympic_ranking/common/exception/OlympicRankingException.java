package com.huawei.olympic_ranking.common.exception;

import java.io.Serial;

public abstract class OlympicRankingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;

    protected OlympicRankingException(final ErrorCode errorCode) {
        super(errorCode.defaultMessage());
        this.errorCode = errorCode;
    }

    protected OlympicRankingException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected OlympicRankingException(final ErrorCode errorCode, final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
