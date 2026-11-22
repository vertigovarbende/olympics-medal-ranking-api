package com.huawei.olympic_ranking.ranking.model;

public record MedalTally(long gold, long silver, long bronze) {

    public static final int GOLD_POINTS = 3;
    public static final int SILVER_POINTS = 2;
    public static final int BRONZE_POINTS = 1;

    public MedalTally {
        if (gold < 0 || silver < 0 || bronze < 0) {
            throw new IllegalArgumentException("Medal counts cannot be negative: gold=%d, silver=%d, bronze=%d".formatted(gold, silver, bronze));
        }
    }

    public long total() {
        return gold + silver + bronze;
    }

    public long weightedPoints() {
        return gold * GOLD_POINTS + silver * SILVER_POINTS + bronze * BRONZE_POINTS;
    }

    public boolean hasMedals() {
        return total() > 0;
    }

}
