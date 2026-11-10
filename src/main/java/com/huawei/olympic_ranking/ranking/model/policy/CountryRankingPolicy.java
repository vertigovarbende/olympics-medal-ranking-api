package com.huawei.olympic_ranking.ranking.model.policy;

import com.huawei.olympic_ranking.ranking.model.MedalTally;
import com.huawei.olympic_ranking.ranking.model.Standing;

import java.util.Comparator;

import static com.huawei.olympic_ranking.ranking.model.policy.StandingOrders.BY_NAME_THEN_ID;
import static com.huawei.olympic_ranking.ranking.model.policy.StandingOrders.most;

public class CountryRankingPolicy implements RankingPolicy {

    private static final Comparator<Standing<?>> ORDER = most(MedalTally::gold)
            .thenComparing(most(MedalTally::silver))
            .thenComparing(most(MedalTally::bronze))
            .thenComparing(BY_NAME_THEN_ID);

    @Override
    public Comparator<Standing<?>> order() {
        return ORDER;
    }

}
