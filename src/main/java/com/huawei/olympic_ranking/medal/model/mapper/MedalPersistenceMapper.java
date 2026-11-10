package com.huawei.olympic_ranking.medal.model.mapper;

import com.huawei.olympic_ranking.athlete.model.entity.AthleteEntity;
import com.huawei.olympic_ranking.athlete.model.mapper.AthletePersistenceMapper;
import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.entity.MedalEntity;
import com.huawei.olympic_ranking.sport.model.entity.SportEntity;
import com.huawei.olympic_ranking.sport.model.mapper.SportPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedalPersistenceMapper {

    private final AthletePersistenceMapper athleteMapper;
    private final SportPersistenceMapper sportMapper;

    public MedalEntity toEntity(final Medal medal, final AthleteEntity athleteEntity, final SportEntity sportEntity) {
        return MedalEntity.builder()
                .id(medal.getId())
                .athlete(athleteEntity)
                .sport(sportEntity)
                .type(medal.getType())
                .build();
    }

    public Medal toDomain(final MedalEntity entity) {
        return Medal.restore(
                entity.getId(),
                athleteMapper.toDomain(entity.getAthlete()),
                sportMapper.toDomain(entity.getSport()),
                entity.getType());
    }

}
