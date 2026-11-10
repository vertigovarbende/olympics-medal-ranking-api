package com.huawei.olympic_ranking.country.model;

import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.common.model.Contender;
import com.huawei.olympic_ranking.country.exception.CountryErrorCode;
import lombok.Getter;

import java.util.Locale;
import java.util.regex.Pattern;

@Getter
public class Country implements Contender {

    public static final int NAME_MAX_LENGTH = 100;

    private static final Pattern CODE_FORMAT = Pattern.compile("[A-Z]{3}");

    private final Long id;
    private final String code;
    private final String name;

    private Country(final Long id, final String code, final String name) {

        if (name == null || name.isBlank()) {
            throw new DomainValidationException(CountryErrorCode.COUNTRY_NAME_REQUIRED);
        }

        if (name.strip().length() > NAME_MAX_LENGTH) {
            throw new DomainValidationException(CountryErrorCode.COUNTRY_NAME_TOO_LONG);
        }

        final String normalizedCode = normalizeCode(code);
        if (!CODE_FORMAT.matcher(normalizedCode).matches()) {
            throw new DomainValidationException(CountryErrorCode.INVALID_COUNTRY_CODE);
        }

        this.id = id;
        this.code = normalizedCode;
        this.name = name.strip();
    }

    public static String normalizeCode(final String code) {
        if (code == null || code.isBlank()) {
            throw new DomainValidationException(CountryErrorCode.COUNTRY_CODE_REQUIRED);
        }
        return code.strip().toUpperCase(Locale.ROOT);
    }

    public static Country of(final String code, final String name) {
        return new Country(null, code, name);
    }

    public static Country restore(final Long id, final String code, final String name) {
        return new Country(id, code, name);
    }

}
