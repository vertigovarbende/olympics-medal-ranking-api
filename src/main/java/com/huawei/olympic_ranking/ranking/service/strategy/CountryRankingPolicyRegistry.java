package com.huawei.olympic_ranking.ranking.service.strategy;

import com.huawei.olympic_ranking.ranking.model.RankingStrategy;
import com.huawei.olympic_ranking.ranking.model.policy.CountryStrategyRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.RankingPolicy;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class CountryRankingPolicyRegistry {

    private final Map<RankingStrategy, RankingPolicy> policies;

    public CountryRankingPolicyRegistry(final List<CountryStrategyRankingPolicy> candidates) {

        final Map<RankingStrategy, RankingPolicy> registered = new EnumMap<>(RankingStrategy.class);

        for (final CountryStrategyRankingPolicy candidate : candidates) {
            if (registered.putIfAbsent(candidate.strategy(), candidate) != null) {
                throw new IllegalStateException("Duplicate country ranking policy for strategy: " + candidate.strategy());
            }
        }

        for (final RankingStrategy strategy : RankingStrategy.values()) {
            if (!registered.containsKey(strategy)) {
                throw new IllegalStateException("No country ranking policy registered for strategy: " + strategy);
            }
        }

        this.policies = registered;
    }

    public RankingPolicy get(final RankingStrategy strategy) {

        Objects.requireNonNull(strategy, "strategy");

        return policies.get(strategy);
    }

}
