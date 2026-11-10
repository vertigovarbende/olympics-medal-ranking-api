package com.huawei.olympic_ranking.country.model.mapper;

import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.model.response.CountryResponse;
import org.springframework.stereotype.Component;

@Component
public class CountryApplicationMapper {

    public CountryResponse toResponse(final Country country) {
        return new CountryResponse(country.getId(), country.getCode(), country.getName());
    }

}
