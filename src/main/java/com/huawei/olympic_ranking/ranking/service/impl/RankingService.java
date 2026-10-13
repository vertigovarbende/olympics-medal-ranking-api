package com.huawei.olympic_ranking.ranking.service.impl;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.Ranking;
import com.huawei.olympic_ranking.ranking.model.RankingStrategy;
import com.huawei.olympic_ranking.ranking.model.policy.AthleteRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.RankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.SportRankingPolicy;
import com.huawei.olympic_ranking.ranking.port.MedalTallyRepository;
import com.huawei.olympic_ranking.ranking.service.RankingUseCase;
import com.huawei.olympic_ranking.ranking.service.strategy.CountryRankingPolicyRegistry;
import com.huawei.olympic_ranking.sport.model.Sport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RankingService implements RankingUseCase {

    private static final RankingPolicy ATHLETE_POLICY = new AthleteRankingPolicy();
    private static final RankingPolicy SPORT_POLICY = new SportRankingPolicy();

    private final MedalTallyRepository medalTallyRepository;
    private final CountryRankingPolicyRegistry countryPolicies;

    @Override
    public Ranking<Country> rankCountries(final RankingStrategy strategy) {

        Objects.requireNonNull(strategy, "strategy");

        final Ranking<Country> ranking = Ranking.of(medalTallyRepository.findCountryTallies(), countryPolicies.get(strategy));

        log.debug("Country ranking calculated: strategy={}, size={}", strategy, ranking.entries().size());

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
