package com.huawei.olympic_ranking.medal.testsupport;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.testsupport.AthleteBuilder;
import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.testsupport.SportBuilder;
import com.huawei.olympic_ranking.testsupport.TestDataBuilder;

public class MedalBuilder extends TestDataBuilder<Medal> {

    private Long id = 1L;
    private Athlete athlete = new AthleteBuilder().build();
    private Sport sport = new SportBuilder().build();
    private MedalType type = MedalType.GOLD;

    public MedalBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public MedalBuilder withoutId() {
        this.id = null;
        return this;
    }

    public MedalBuilder withAthlete(final Athlete athlete) {
        this.athlete = athlete;
        return this;
    }

    public MedalBuilder withSport(final Sport sport) {
        this.sport = sport;
        return this;
    }

    public MedalBuilder withType(final MedalType type) {
        this.type = type;
        return this;
    }

    @Override
    public Medal build() {
        return id == null ? Medal.of(athlete, sport, type) : Medal.restore(id, athlete, sport, type);
    }

}
