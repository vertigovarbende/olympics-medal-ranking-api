package com.huawei.olympic_ranking.medal.model;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.medal.exception.MedalErrorCode;
import com.huawei.olympic_ranking.sport.model.Sport;
import lombok.Getter;

@Getter
public class Medal {

    private final Long id;
    private final Athlete athlete;
    private final Sport sport;
    private final MedalType type;

    private Medal(final Long id, final Athlete athlete, final Sport sport, final MedalType type) {

        if (athlete == null) {
            throw new DomainValidationException(MedalErrorCode.MEDAL_ATHLETE_REQUIRED);
        }

        if (sport == null) {
            throw new DomainValidationException(MedalErrorCode.MEDAL_SPORT_REQUIRED);
        }

        if (type == null) {
            throw new DomainValidationException(MedalErrorCode.MEDAL_TYPE_REQUIRED);
        }

        this.id = id;
        this.athlete = athlete;
        this.sport = sport;
        this.type = type;
    }
    public static Medal of(final Athlete athlete, final Sport sport, final MedalType type) {
        return new Medal(null, athlete, sport, type);
    }

    public static Medal restore(final Long id, final Athlete athlete, final Sport sport, final MedalType type) {
        return new Medal(id, athlete, sport, type);
    }

    public Country country() {
        return athlete.getCountry();
    }

}
