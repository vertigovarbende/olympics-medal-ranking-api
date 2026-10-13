package com.huawei.olympic_ranking.common.testsupport;

import com.huawei.olympic_ranking.common.exception.ErrorCode;
import com.huawei.olympic_ranking.common.exception.OlympicRankingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@TestComponent
@RestController
@RequestMapping(ErrorProbeController.PATH)
public class ErrorProbeController {

    public static final String PATH = "/test-errors";

    public static final String SERVER_ERROR_CODE = "PRB_001";
    public static final String SERVER_ERROR_HEADER = "PROBE_SERVER_ERROR";
    public static final String SERVER_ERROR_MESSAGE = "Probe failure.";

    public static final String LEAKED_DETAIL = "internal detail must not leak";

    private final Validator validator;

    public ErrorProbeController(final Validator validator) {
        this.validator = validator;
    }

    @GetMapping("/typed/{id}")
    public String typed(@PathVariable final Long id) {
        return String.valueOf(id);
    }

    @GetMapping("/constraint-violation")
    public String constraintViolation() {

        final Set<ConstraintViolation<Probe>> violations = this.validator.validate(new Probe(" "));

        throw new ConstraintViolationException(violations);
    }

    @GetMapping("/data-conflict")
    public String dataConflict() {
        throw new DataIntegrityViolationException(LEAKED_DETAIL);
    }

    @GetMapping("/server-error")
    public String serverError() {
        throw new ProbeServerException();
    }

    @GetMapping("/unexpected")
    public String unexpected() {
        throw new IllegalStateException(LEAKED_DETAIL);
    }

    private static class Probe {

        @NotBlank
        private final String name;

        Probe(final String name) {
            this.name = name;
        }

    }

    private record ProbeErrorCode(String code, String header, String defaultMessage) implements ErrorCode {
    }

    private static class ProbeServerException extends OlympicRankingException {

        ProbeServerException() {
            super(new ProbeErrorCode(SERVER_ERROR_CODE, SERVER_ERROR_HEADER, SERVER_ERROR_MESSAGE));
        }

    }

}
