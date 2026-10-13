package com.huawei.olympic_ranking.ranking.repository.projection;

public record AthleteTallyRow(
        Long id,
        String name,
        Long countryId,
        String countryCode,
        String countryName,
        Long gold,
        Long silver,
        Long bronze
) {

}
