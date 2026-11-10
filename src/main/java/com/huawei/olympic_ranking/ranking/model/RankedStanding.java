package com.huawei.olympic_ranking.ranking.model;

import com.huawei.olympic_ranking.common.model.Contender;

import java.util.Objects;

public record RankedStanding<T extends Contender>(int rank, Standing<T> standing) {

    public RankedStanding {
        if (rank < 1) {
            throw new IllegalArgumentException("Rank must be 1 or greater, was " + rank);
        }

        Objects.requireNonNull(standing, "standing");
    }

}
