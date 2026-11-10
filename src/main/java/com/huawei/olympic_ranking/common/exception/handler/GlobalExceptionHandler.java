package com.huawei.olympic_ranking.common.exception.handler;

import com.huawei.olympic_ranking.common.exception.CommonErrorCode;
import com.huawei.olympic_ranking.common.exception.OlympicRankingDuplicateException;
import com.huawei.olympic_ranking.common.exception.OlympicRankingException;
import com.huawei.olympic_ranking.common.exception.OlympicRankingResourceNotFoundException;
import com.huawei.olympic_ranking.common.exception.OlympicRankingValidationException;
import com.huawei.olympic_ranking.common.model.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.subErrors(exception.getBindingResult().getFieldErrors())
                .header(CommonErrorCode.VALIDATION_ERROR.header())
                .code(CommonErrorCode.VALIDATION_ERROR.code())
                .message(CommonErrorCode.VALIDATION_ERROR.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logClientError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.subErrors(exception.getConstraintViolations())
                .header(CommonErrorCode.VALIDATION_ERROR.header())
                .code(CommonErrorCode.VALIDATION_ERROR.code())
                .message(CommonErrorCode.VALIDATION_ERROR.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logClientError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.subErrors(exception)
                .header(CommonErrorCode.VALIDATION_ERROR.header())
                .code(CommonErrorCode.VALIDATION_ERROR.code())
                .message(CommonErrorCode.VALIDATION_ERROR.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logClientError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .header(CommonErrorCode.MALFORMED_REQUEST.header())
                .code(CommonErrorCode.MALFORMED_REQUEST.code())
                .message(CommonErrorCode.MALFORMED_REQUEST.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logClientError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .header(CommonErrorCode.METHOD_NOT_ALLOWED.header())
                .code(CommonErrorCode.METHOD_NOT_ALLOWED.code())
                .message(CommonErrorCode.METHOD_NOT_ALLOWED.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logClientError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .header(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE.header())
                .code(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE.code())
                .message(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logClientError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoResourceFoundException(final NoResourceFoundException exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .header(CommonErrorCode.RESOURCE_NOT_FOUND.header())
                .code(CommonErrorCode.RESOURCE_NOT_FOUND.code())
                .message(CommonErrorCode.RESOURCE_NOT_FOUND.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logClientError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .header(CommonErrorCode.DATA_CONFLICT.header())
                .code(CommonErrorCode.DATA_CONFLICT.code())
                .message(CommonErrorCode.DATA_CONFLICT.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logClientError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(OlympicRankingResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOlympicRankingResourceNotFoundException(OlympicRankingResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponse response = this.toErrorResponse(ex, request);

        this.logClientError(ex, response);

        return response;
    }

    @ExceptionHandler(OlympicRankingDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOlympicRankingDuplicateException(OlympicRankingDuplicateException ex, HttpServletRequest request) {

        ErrorResponse response = this.toErrorResponse(ex, request);

        this.logClientError(ex, response);

        return response;
    }

    @ExceptionHandler(OlympicRankingValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOlympicRankingValidationException(OlympicRankingValidationException ex, HttpServletRequest request) {

        ErrorResponse response = this.toErrorResponse(ex, request);

        this.logClientError(ex, response);

        return response;
    }

    @ExceptionHandler(OlympicRankingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOlympicRankingException(final OlympicRankingException exception, HttpServletRequest request) {

        final ErrorResponse errorResponse = this.toErrorResponse(exception, request);

        this.logServerError(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception exception, final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .header(CommonErrorCode.INTERNAL_SERVER_ERROR.header())
                .code(CommonErrorCode.INTERNAL_SERVER_ERROR.code())
                .message(CommonErrorCode.INTERNAL_SERVER_ERROR.defaultMessage())
                .path(request.getRequestURI())
                .build();

        this.logServerError(exception, errorResponse);

        return errorResponse;
    }

    private ErrorResponse toErrorResponse(final OlympicRankingException exception, HttpServletRequest request) {

        return ErrorResponse.builder()
                .header(exception.getErrorCode().header())
                .code(exception.getErrorCode().code())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    private void logClientError(final Exception exception, final ErrorResponse errorResponse) {

        log.warn("responseCode:{} | path:{} | {}", errorResponse.getCode(), errorResponse.getPath(), exception.getMessage());
        log.debug("responseCode:{} | StackTrace:", errorResponse.getCode(), exception);

    }

    private void logServerError(final Exception exception, final ErrorResponse errorResponse) {

        log.error("responseCode:{} | path:{} | {}", errorResponse.getCode(), errorResponse.getPath(), exception.getMessage(), exception);

    }

}
