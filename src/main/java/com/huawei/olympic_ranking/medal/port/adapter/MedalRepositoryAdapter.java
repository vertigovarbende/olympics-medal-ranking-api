package com.huawei.olympic_ranking.medal.port.adapter;

import com.huawei.olympic_ranking.athlete.model.entity.AthleteEntity;
import com.huawei.olympic_ranking.athlete.repository.SpringDataAthleteRepository;
import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.medal.model.mapper.MedalPersistenceMapper;
import com.huawei.olympic_ranking.medal.port.MedalRepository;
import com.huawei.olympic_ranking.medal.repository.SpringDataMedalRepository;
import com.huawei.olympic_ranking.sport.model.entity.SportEntity;
import com.huawei.olympic_ranking.sport.repository.SpringDataSportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MedalRepositoryAdapter implements MedalRepository {

    private final SpringDataMedalRepository repository;
    private final SpringDataAthleteRepository athleteRepository;
    private final SpringDataSportRepository sportRepository;
    private final MedalPersistenceMapper mapper;

    @Override
    @Transactional
    public Medal save(final Medal medal) {
        final AthleteEntity athleteEntity = athleteRepository.getReferenceById(medal.getAthlete().getId());
        final SportEntity sportEntity = sportRepository.getReferenceById(medal.getSport().getId());
        final Medal saved = mapper.toDomain(repository.save(mapper.toEntity(medal, athleteEntity, sportEntity)));

        log.debug("Medal persisted: id={}, athleteId={}, sportId={}, type={}",
                saved.getId(), saved.getAthlete().getId(), saved.getSport().getId(), saved.getType());

        return saved;
    }

    @Override
    public boolean existsByAthleteIdAndSportIdAndType(final Long athleteId, final Long sportId, final MedalType type) {
        final boolean exists = repository.existsByAthleteIdAndSportIdAndType(athleteId, sportId, type);
        log.debug("Medal exists check: athleteId={}, sportId={}, type={}, exists={}", athleteId, sportId, type, exists);

        return exists;
    }

}
