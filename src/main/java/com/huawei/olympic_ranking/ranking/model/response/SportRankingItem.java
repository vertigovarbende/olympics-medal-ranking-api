package com.huawei.olympic_ranking.ranking.model.response;

public record SportRankingItem(
        int rank,
        Long sportId,
        String sportName,
        long total
) {

}
