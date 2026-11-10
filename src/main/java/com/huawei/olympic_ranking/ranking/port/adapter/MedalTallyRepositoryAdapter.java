package com.huawei.olympic_ranking.ranking.port.adapter;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.MedalTally;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.port.MedalTallyRepository;
import com.huawei.olympic_ranking.ranking.repository.SpringDataRankingRepository;
import com.huawei.olympic_ranking.sport.model.Sport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MedalTallyRepositoryAdapter implements MedalTallyRepository {

    private final SpringDataRankingRepository repository;

    @Override
    public List<Standing<Country>> findCountryTallies() {
        final List<Standing<Country>> tallies = repository.tallyByCountry().stream()
                .map(row -> new Standing<>(
                        Country.restore(row.id(), row.code(), row.name()),
                        new MedalTally(row.gold(), row.silver(), row.bronze())))
                .toList();
        log.debug("Country medal tallies loaded: count={}", tallies.size());
        return tallies;
    }

    @Override
    public List<Standing<Athlete>> findAthleteTallies() {
        final List<Standing<Athlete>> tallies = repository.tallyByAthlete().stream()
                .map(row -> new Standing<>(
                        Athlete.restore(row.id(), row.name(),
                                Country.restore(row.countryId(), row.countryCode(), row.countryName())),
                        new MedalTally(row.gold(), row.silver(), row.bronze())))
                .toList();
        log.debug("Athlete medal tallies loaded: count={}", tallies.size());
        return tallies;
    }

    @Override
    public List<Standing<Sport>> findSportTallies() {
        final List<Standing<Sport>> tallies = repository.tallyBySport().stream()
                .map(row -> new Standing<>(
                        Sport.restore(row.id(), row.name()),
                        new MedalTally(row.gold(), row.silver(), row.bronze())))
                .toList();

        log.debug("Sport medal tallies loaded: count={}", tallies.size());
        return tallies;
    }

}
