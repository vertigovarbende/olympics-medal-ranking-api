package com.huawei.olympic_ranking.ranking.model.response;

public record AthleteRankingItem(
        int rank,
        Long athleteId,
        String athleteName,
        long total,
        long gold,
        long silver,
        long bronze
) {

}
