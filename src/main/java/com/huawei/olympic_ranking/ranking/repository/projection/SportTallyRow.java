package com.huawei.olympic_ranking.ranking.repository.projection;

public record SportTallyRow(
        Long id,
        String name,
        Long gold,
        Long silver,
        Long bronze
) {

}
