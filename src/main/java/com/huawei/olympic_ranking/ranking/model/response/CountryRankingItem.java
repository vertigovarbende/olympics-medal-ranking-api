package com.huawei.olympic_ranking.ranking.model.response;

public record CountryRankingItem(
        int rank,
        String countryCode,
        String countryName,
        long gold,
        long silver,
        long bronze,
        long total
) {

}
