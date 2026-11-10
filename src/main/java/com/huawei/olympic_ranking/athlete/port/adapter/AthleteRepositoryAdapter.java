package com.huawei.olympic_ranking.athlete.port.adapter;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.model.mapper.AthletePersistenceMapper;
import com.huawei.olympic_ranking.athlete.port.AthleteRepository;
import com.huawei.olympic_ranking.athlete.repository.SpringDataAthleteRepository;
import com.huawei.olympic_ranking.country.model.entity.CountryEntity;
import com.huawei.olympic_ranking.country.repository.SpringDataCountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AthleteRepositoryAdapter implements AthleteRepository {

    private final SpringDataAthleteRepository repository;
    private final SpringDataCountryRepository countryRepository;
    private final AthletePersistenceMapper mapper;

    @Override
    @Transactional
    public Athlete save(final Athlete athlete) {

        final CountryEntity countryEntity = countryRepository.getReferenceById(athlete.getCountry().getId());
        final Athlete saved = mapper.toDomain(repository.save(mapper.toEntity(athlete, countryEntity)));

        log.debug("Athlete persisted: id={}, countryId={}", saved.getId(), saved.getCountry().getId());
        return saved;
    }

    @Override
    public Optional<Athlete> findById(final Long id) {
        final Optional<Athlete> athlete = repository.findById(id).map(mapper::toDomain);

        log.debug("Athlete lookup by id: id={}, found={}", id, athlete.isPresent());
        return athlete;
    }

}
