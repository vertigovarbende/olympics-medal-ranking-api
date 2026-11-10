package com.huawei.olympic_ranking.sport.model.mapper;

import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.model.entity.SportEntity;
import org.springframework.stereotype.Component;

@Component
public class SportPersistenceMapper {

    public SportEntity toEntity(final Sport sport) {
        return SportEntity.builder()
                .id(sport.getId())
                .name(sport.getName())
                .build();
    }

    public Sport toDomain(final SportEntity entity) {
        return Sport.restore(entity.getId(), entity.getName());
    }

}
