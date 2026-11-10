package com.huawei.olympic_ranking.country.exception;

import com.huawei.olympic_ranking.common.exception.OlympicRankingDuplicateException;

import java.io.Serial;

public class DuplicateCountryException extends OlympicRankingDuplicateException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateCountryException(final String code) {
        super(CountryErrorCode.COUNTRY_CODE_ALREADY_EXISTS, "Country already exists with code: " + code);
    }

}
