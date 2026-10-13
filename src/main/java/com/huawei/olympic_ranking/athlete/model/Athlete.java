package com.huawei.olympic_ranking.athlete.model;

import com.huawei.olympic_ranking.athlete.exception.AthleteErrorCode;
import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.common.model.Contender;
import com.huawei.olympic_ranking.country.model.Country;
import lombok.Getter;

@Getter
public class Athlete implements Contender {

    public static final int NAME_MAX_LENGTH = 100;

    private final Long id;
    private final String name;
    private final Country country;

    private Athlete(final Long id, final String name, final Country country) {

        if (name == null || name.isBlank()) {
            throw new DomainValidationException(AthleteErrorCode.ATHLETE_NAME_REQUIRED);
        }

        if (name.strip().length() > NAME_MAX_LENGTH) {
            throw new DomainValidationException(AthleteErrorCode.ATHLETE_NAME_TOO_LONG);
        }

        if (country == null) {
            throw new DomainValidationException(AthleteErrorCode.ATHLETE_COUNTRY_REQUIRED);
        }

        this.id = id;
        this.name = name.strip();
        this.country = country;
    }

    public static Athlete of(final String name, final Country country) {
        return new Athlete(null, name, country);
    }

    public static Athlete restore(final Long id, final String name, final Country country) {
        return new Athlete(id, name, country);
    }

}
