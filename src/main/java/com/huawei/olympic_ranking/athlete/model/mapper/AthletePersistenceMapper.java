package com.huawei.olympic_ranking.athlete.model.mapper;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.model.entity.AthleteEntity;
import com.huawei.olympic_ranking.country.model.entity.CountryEntity;
import com.huawei.olympic_ranking.country.model.mapper.CountryPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AthletePersistenceMapper {

    private final CountryPersistenceMapper countryMapper;

    public AthleteEntity toEntity(final Athlete athlete, final CountryEntity countryEntity) {
        return AthleteEntity.builder()
                .id(athlete.getId())
                .name(athlete.getName())
                .country(countryEntity)
                .build();
    }

    public Athlete toDomain(final AthleteEntity entity) {
        return Athlete.restore(entity.getId(), entity.getName(), countryMapper.toDomain(entity.getCountry()));
    }

}
