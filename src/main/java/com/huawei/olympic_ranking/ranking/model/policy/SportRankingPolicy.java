package com.huawei.olympic_ranking.ranking.model.policy;

import com.huawei.olympic_ranking.ranking.model.Standing;

import java.util.Comparator;

import static com.huawei.olympic_ranking.ranking.model.policy.StandingOrders.BY_NAME_THEN_ID;
import static com.huawei.olympic_ranking.ranking.model.policy.StandingOrders.MOST_MEDALS_FIRST;

public class SportRankingPolicy implements RankingPolicy {

    private static final Comparator<Standing<?>> ORDER = MOST_MEDALS_FIRST.thenComparing(BY_NAME_THEN_ID);

    @Override
    public Comparator<Standing<?>> order() {
        return ORDER;
    }

}
