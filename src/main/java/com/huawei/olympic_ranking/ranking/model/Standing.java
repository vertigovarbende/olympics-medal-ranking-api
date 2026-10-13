package com.huawei.olympic_ranking.ranking.model;

import com.huawei.olympic_ranking.common.model.Contender;

import java.util.Objects;

public record Standing<T extends Contender>(T contender, MedalTally tally) {

    public Standing {
        Objects.requireNonNull(contender, "contender");
        Objects.requireNonNull(tally, "tally");
    }

    public String name() {
        return contender.getName();
    }

    public Long id() {
        return contender.getId();
    }

    public boolean hasMedals() {
        return tally.hasMedals();
    }

}
