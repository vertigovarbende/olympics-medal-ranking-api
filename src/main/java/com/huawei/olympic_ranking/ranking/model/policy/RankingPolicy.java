package com.huawei.olympic_ranking.ranking.model.policy;

import com.huawei.olympic_ranking.ranking.model.Standing;

import java.util.Comparator;

public interface RankingPolicy {

    Comparator<Standing<?>> order();

}
