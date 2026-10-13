package com.huawei.olympic_ranking.medal.service.impl;

import com.huawei.olympic_ranking.athlete.exception.AthleteNotFoundException;
import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.port.AthleteRepository;
import com.huawei.olympic_ranking.medal.exception.DuplicateMedalException;
import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.medal.port.MedalRepository;
import com.huawei.olympic_ranking.medal.service.MedalUseCase;
import com.huawei.olympic_ranking.sport.exception.SportNotFoundException;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.port.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedalService implements MedalUseCase {

    private final MedalRepository medalRepository;
    private final AthleteRepository athleteRepository;
    private final SportRepository sportRepository;

    @Override
    @Transactional
    public Medal create(final Long athleteId, final Long sportId, final MedalType type) {

        log.debug("Creating medal: athleteId={}, sportId={}, type={}", athleteId, sportId, type);

        final Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new AthleteNotFoundException(athleteId));

        final Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new SportNotFoundException(sportId));

        if (medalRepository.existsByAthleteIdAndSportIdAndType(athleteId, sportId, type)) {
            throw new DuplicateMedalException(athleteId, sportId, type);
        }

        final Medal saved = medalRepository.save(Medal.of(athlete, sport, type));
        log.info("Medal created: id={}, athleteId={}, sportId={}, type={}, countryCode={}", saved.getId(), athleteId, sportId, type, saved.country().getCode());
        return saved;

    }

}
