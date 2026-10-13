package com.huawei.olympic_ranking.country.service;

import com.huawei.olympic_ranking.country.model.Country;

public interface CountryUseCase {

    Country create(String name, String code);

}
