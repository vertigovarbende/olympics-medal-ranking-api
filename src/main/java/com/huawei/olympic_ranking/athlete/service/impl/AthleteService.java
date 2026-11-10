package com.huawei.olympic_ranking.athlete.service.impl;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.port.AthleteRepository;
import com.huawei.olympic_ranking.athlete.service.AthleteUseCase;
import com.huawei.olympic_ranking.country.exception.CountryNotFoundException;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.port.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AthleteService implements AthleteUseCase {

    private final AthleteRepository athleteRepository;
    private final CountryRepository countryRepository;

    @Override
    @Transactional
    public Athlete create(final String name, final String countryCode) {

        final String normalizedCode = Country.normalizeCode(countryCode);
        log.debug("Creating athlete: countryCode={}", normalizedCode);

        final Country country = countryRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new CountryNotFoundException(normalizedCode));

        final Athlete saved = athleteRepository.save(Athlete.of(name, country));
        log.info("Athlete created: id={}, countryCode={}", saved.getId(), country.getCode());

        return saved;
    }

}
