package com.huawei.olympic_ranking.ranking.testsupport;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.common.model.Contender;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import com.huawei.olympic_ranking.ranking.model.MedalTally;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.testsupport.TestDataBuilder;

public class StandingBuilder<T extends Contender> extends TestDataBuilder<Standing<T>> {

    private final T contender;
    private long gold;
    private long silver;
    private long bronze;

    private StandingBuilder(final T contender) {
        this.contender = contender;
    }

    public static <T extends Contender> StandingBuilder<T> of(final T contender) {
        return new StandingBuilder<>(contender);
    }

    public static StandingBuilder<Country> aCountry(final String name) {
        return of(new CountryBuilder().withId(nextId()).withCode(uniqueCode()).withName(name).build());
    }

    public static StandingBuilder<Athlete> anAthlete(final String name) {
        return of(Athlete.restore(nextId(), name, new CountryBuilder().build()));
    }

    public static StandingBuilder<Sport> aSport(final String name) {
        return of(Sport.restore(nextId(), name));
    }

    public StandingBuilder<T> withMedals(final long gold, final long silver, final long bronze) {
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        return this;
    }

    @Override
    public Standing<T> build() {
        return new Standing<>(contender, new MedalTally(gold, silver, bronze));
    }

}
