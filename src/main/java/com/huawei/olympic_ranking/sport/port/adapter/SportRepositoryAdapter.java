package com.huawei.olympic_ranking.sport.port.adapter;

import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.model.mapper.SportPersistenceMapper;
import com.huawei.olympic_ranking.sport.port.SportRepository;
import com.huawei.olympic_ranking.sport.repository.SpringDataSportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SportRepositoryAdapter implements SportRepository {

    private final SpringDataSportRepository repository;
    private final SportPersistenceMapper mapper;

    @Override
    @Transactional
    public Sport save(final Sport sport) {
        final Sport saved = mapper.toDomain(repository.save(mapper.toEntity(sport)));
        log.debug("Sport persisted: id={}, name={}", saved.getId(), saved.getName());

        return saved;
    }

    @Override
    public Optional<Sport> findById(final Long id) {
        final Optional<Sport> sport = repository.findById(id).map(mapper::toDomain);
        log.debug("Sport lookup by id: id={}, found={}", id, sport.isPresent());

        return sport;
    }

    @Override
    public boolean existsByNameIgnoreCase(final String name) {
        final boolean exists = repository.existsByNameIgnoreCase(name);
        log.debug("Sport exists check: name={}, exists={}", name, exists);

        return exists;
    }

}
