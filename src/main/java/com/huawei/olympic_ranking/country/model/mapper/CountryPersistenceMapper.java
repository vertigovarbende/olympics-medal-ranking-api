package com.huawei.olympic_ranking.country.model.mapper;

import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.model.entity.CountryEntity;
import org.springframework.stereotype.Component;

@Component
public class CountryPersistenceMapper {

    public CountryEntity toEntity(final Country country) {
        return CountryEntity.builder()
                .id(country.getId())
                .code(country.getCode())
                .name(country.getName())
                .build();
    }

    public Country toDomain(final CountryEntity entity) {
        return Country.restore(entity.getId(), entity.getCode(), entity.getName());
    }

}
