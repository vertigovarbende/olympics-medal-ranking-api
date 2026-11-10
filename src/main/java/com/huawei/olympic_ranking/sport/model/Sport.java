package com.huawei.olympic_ranking.sport.model;

import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.common.model.Contender;
import com.huawei.olympic_ranking.sport.exception.SportErrorCode;
import lombok.Getter;

@Getter
public class Sport implements Contender {

    public static final int NAME_MAX_LENGTH = 100;

    private final Long id;
    private final String name;

    private Sport(final Long id, final String name) {

        if (name == null || name.isBlank()) {
            throw new DomainValidationException(SportErrorCode.SPORT_NAME_REQUIRED);
        }

        if (name.strip().length() > NAME_MAX_LENGTH) {
            throw new DomainValidationException(SportErrorCode.SPORT_NAME_TOO_LONG);
        }

        this.id = id;
        this.name = name.strip();
    }

    public static Sport of(final String name) {
        return new Sport(null, name);
    }

    public static Sport restore(final Long id, final String name) {
        return new Sport(id, name);
    }

}
