package com.huawei.olympic_ranking.ranking.port;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.sport.model.Sport;

import java.util.List;

public interface MedalTallyRepository {

    List<Standing<Country>> findCountryTallies();

    List<Standing<Athlete>> findAthleteTallies();

    List<Standing<Sport>> findSportTallies();

}
