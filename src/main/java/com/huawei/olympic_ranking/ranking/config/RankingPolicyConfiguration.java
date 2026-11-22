package com.huawei.olympic_ranking.ranking.config;

import com.huawei.olympic_ranking.ranking.model.policy.CountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.CountryStrategyRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.WeightedCountryRankingPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RankingPolicyConfiguration {

    @Bean
    public CountryStrategyRankingPolicy officialCountryRankingPolicy() {
        return new CountryRankingPolicy();
    }

    @Bean
    public CountryStrategyRankingPolicy weightedCountryRankingPolicy() {
        return new WeightedCountryRankingPolicy();
    }

}
