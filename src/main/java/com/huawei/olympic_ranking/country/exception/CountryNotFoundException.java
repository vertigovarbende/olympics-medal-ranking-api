package com.huawei.olympic_ranking.country.exception;

import com.huawei.olympic_ranking.common.exception.OlympicRankingResourceNotFoundException;

import java.io.Serial;

public class CountryNotFoundException extends OlympicRankingResourceNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CountryNotFoundException(final String code) {
        super(CountryErrorCode.COUNTRY_NOT_FOUND, "Country not found with code: " + code);
    }

}
