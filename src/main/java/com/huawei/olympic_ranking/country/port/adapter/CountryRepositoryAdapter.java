package com.huawei.olympic_ranking.country.port.adapter;

import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.model.mapper.CountryPersistenceMapper;
import com.huawei.olympic_ranking.country.port.CountryRepository;
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
public class CountryRepositoryAdapter implements CountryRepository {

    private final SpringDataCountryRepository repository;
    private final CountryPersistenceMapper mapper;

    @Override
    @Transactional
    public Country save(final Country country) {
        final Country saved = mapper.toDomain(repository.save(mapper.toEntity(country)));
        log.debug("Country persisted: id={}, code={}", saved.getId(), saved.getCode());

        return saved;
    }

    @Override
    public boolean existsByCode(final String code) {
        final boolean exists = repository.existsByCode(code);
        log.debug("Country exists check: code={}, exists={}", code, exists);

        return exists;
    }

    @Override
    public Optional<Country> findByCode(final String code) {
        final Optional<Country> country = repository.findByCode(code).map(mapper::toDomain);
        log.debug("Country lookup by code: code={}, found={}", code, country.isPresent());

        return country;
    }

}
