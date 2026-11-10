package com.huawei.olympic_ranking.country.service.impl;

import com.huawei.olympic_ranking.country.exception.DuplicateCountryException;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.port.CountryRepository;
import com.huawei.olympic_ranking.country.service.CountryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryService implements CountryUseCase {

    private final CountryRepository countryRepository;

    @Override
    @Transactional
    public Country create(final String name, final String code) {

        final Country country = Country.of(code, name);

        log.debug("Creating country: code={}", country.getCode());

        if (countryRepository.existsByCode(country.getCode())) {
            throw new DuplicateCountryException(country.getCode());
        }

        final Country saved = countryRepository.save(country);
        log.info("Country created: id={}, code={}", saved.getId(), saved.getCode());

        return saved;
    }

}
