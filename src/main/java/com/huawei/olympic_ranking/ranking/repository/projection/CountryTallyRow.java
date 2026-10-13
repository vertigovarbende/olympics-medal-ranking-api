package com.huawei.olympic_ranking.ranking.repository.projection;

public record CountryTallyRow(
        Long id,
        String code,
        String name,
        Long gold,
        Long silver,
        Long bronze
) {

}
