package com.huawei.olympic_ranking.ranking.service.impl;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.Ranking;
import com.huawei.olympic_ranking.ranking.model.policy.AthleteRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.CountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.RankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.SportRankingPolicy;
import com.huawei.olympic_ranking.ranking.port.MedalTallyRepository;
import com.huawei.olympic_ranking.ranking.service.RankingUseCase;
import com.huawei.olympic_ranking.sport.model.Sport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RankingService implements RankingUseCase {

    private static final RankingPolicy COUNTRY_POLICY = new CountryRankingPolicy();
    private static final RankingPolicy ATHLETE_POLICY = new AthleteRankingPolicy();
    private static final RankingPolicy SPORT_POLICY = new SportRankingPolicy();

    private final MedalTallyRepository medalTallyRepository;

    @Override
    public Ranking<Country> rankCountries() {

        final Ranking<Country> ranking = Ranking.of(medalTallyRepository.findCountryTallies(), COUNTRY_POLICY);

        log.debug("Country ranking calculated: size={}", ranking.entries().size());

        return ranking;
    }

    @Override
    public Ranking<Athlete> rankAthletes() {

        final Ranking<Athlete> ranking = Ranking.of(medalTallyRepository.findAthleteTallies(), ATHLETE_POLICY);

        log.debug("Athlete ranking calculated: size={}", ranking.entries().size());

        return ranking;
    }

    @Override
    public Ranking<Sport> rankSports() {

        final Ranking<Sport> ranking = Ranking.of(medalTallyRepository.findSportTallies(), SPORT_POLICY);

        log.debug("Sport ranking calculated: size={}", ranking.entries().size());

        return ranking;
    }

}
