package com.huawei.olympic_ranking.sport.service.impl;

import com.huawei.olympic_ranking.sport.exception.DuplicateSportException;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.port.SportRepository;
import com.huawei.olympic_ranking.sport.service.SportUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SportService implements SportUseCase {

    private final SportRepository sportRepository;

    @Override
    @Transactional
    public Sport create(final String name) {

        final Sport sport = Sport.of(name);

        log.debug("Creating sport: name={}", sport.getName());

        if (sportRepository.existsByNameIgnoreCase(sport.getName())) {
            throw new DuplicateSportException(sport.getName());
        }

        final Sport saved = sportRepository.save(sport);
        log.info("Sport created: id={}, name={}", saved.getId(), saved.getName());

        return saved;
    }

}
