package com.huawei.olympic_ranking.ranking.model.mapper;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.MedalTally;
import com.huawei.olympic_ranking.ranking.model.RankedStanding;
import com.huawei.olympic_ranking.ranking.model.Ranking;
import com.huawei.olympic_ranking.ranking.model.response.AthleteRankingItem;
import com.huawei.olympic_ranking.ranking.model.response.CountryRankingItem;
import com.huawei.olympic_ranking.ranking.model.response.SportRankingItem;
import com.huawei.olympic_ranking.sport.model.Sport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RankingApplicationMapper {

    public CountryRankingItem toCountryItem(final RankedStanding<Country> ranked) {
        final Country country = ranked.standing().contender();
        final MedalTally tally = ranked.standing().tally();
        return new CountryRankingItem(
                ranked.rank(),
                country.getCode(),
                country.getName(),
                tally.gold(),
                tally.silver(),
                tally.bronze(),
                tally.total());
    }

    public List<CountryRankingItem> toCountryItems(final Ranking<Country> ranking) {
        return ranking.entries().stream().map(this::toCountryItem).toList();
    }

    public AthleteRankingItem toAthleteItem(final RankedStanding<Athlete> ranked) {
        final Athlete athlete = ranked.standing().contender();
        final MedalTally tally = ranked.standing().tally();
        return new AthleteRankingItem(
                ranked.rank(),
                athlete.getId(),
                athlete.getName(),
                tally.total(),
                tally.gold(),
                tally.silver(),
                tally.bronze()
        );
    }

    public List<AthleteRankingItem> toAthleteItems(final Ranking<Athlete> ranking) {
        return ranking.entries().stream().map(this::toAthleteItem).toList();
    }

    public SportRankingItem toSportItem(final RankedStanding<Sport> ranked) {
        final Sport sport = ranked.standing().contender();
        return new SportRankingItem(
                ranked.rank(),
                sport.getId(),
                sport.getName(),
                ranked.standing().tally().total()
        );
    }

    public List<SportRankingItem> toSportItems(final Ranking<Sport> ranking) {
        return ranking.entries().stream().map(this::toSportItem).toList();
    }

}
