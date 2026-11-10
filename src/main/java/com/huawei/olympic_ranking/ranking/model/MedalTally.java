package com.huawei.olympic_ranking.ranking.model;

public record MedalTally(long gold, long silver, long bronze) {

    public MedalTally {
        if (gold < 0 || silver < 0 || bronze < 0) {
            throw new IllegalArgumentException("Medal counts cannot be negative: gold=%d, silver=%d, bronze=%d".formatted(gold, silver, bronze));
        }
    }

    public long total() {
        return gold + silver + bronze;
    }

    public boolean hasMedals() {
        return total() > 0;
    }

}
