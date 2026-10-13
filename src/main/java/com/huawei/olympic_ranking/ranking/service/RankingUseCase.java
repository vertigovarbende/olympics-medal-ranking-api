package com.huawei.olympic_ranking.ranking.service;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.Ranking;
import com.huawei.olympic_ranking.ranking.model.RankingStrategy;
import com.huawei.olympic_ranking.sport.model.Sport;

public interface RankingUseCase {

    Ranking<Country> rankCountries(RankingStrategy strategy);

    Ranking<Athlete> rankAthletes();

    Ranking<Sport> rankSports();

}
