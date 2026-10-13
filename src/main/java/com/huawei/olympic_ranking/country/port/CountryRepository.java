package com.huawei.olympic_ranking.country.port;

import com.huawei.olympic_ranking.country.model.Country;

import java.util.Optional;

public interface CountryRepository {

    Country save(Country country);

    boolean existsByCode(String code);

    Optional<Country> findByCode(String code);

}
