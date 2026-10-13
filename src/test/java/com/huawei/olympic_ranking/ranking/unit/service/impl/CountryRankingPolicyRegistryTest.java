package com.huawei.olympic_ranking.ranking.unit.service.impl;

import com.huawei.olympic_ranking.ranking.model.RankingStrategy;
import com.huawei.olympic_ranking.ranking.model.policy.CountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.CountryStrategyRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.WeightedCountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.service.strategy.CountryRankingPolicyRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CountryRankingPolicyRegistry - Birim Testleri")
class CountryRankingPolicyRegistryTest {

    private final CountryStrategyRankingPolicy official = new CountryRankingPolicy();

    private final CountryStrategyRankingPolicy weighted = new WeightedCountryRankingPolicy();

    @Test
    @DisplayName("get() her stratejiye kayitli politikayi dondurur")
    void shouldReturnRegisteredPolicy_WhenStrategyIsKnown() {

        final CountryRankingPolicyRegistry registry = new CountryRankingPolicyRegistry(List.of(this.official, this.weighted));

        assertThat(registry.get(RankingStrategy.OFFICIAL)).isSameAs(this.official);
        assertThat(registry.get(RankingStrategy.WEIGHTED)).isSameAs(this.weighted);

    }

    @Test
    @DisplayName("get() kayit sirasindan bagimsiz calisir")
    void shouldReturnRegisteredPolicy_WhenCandidatesAreInAnyOrder() {

        final CountryRankingPolicyRegistry registry = new CountryRankingPolicyRegistry(List.of(this.weighted, this.official));

        assertThat(registry.get(RankingStrategy.OFFICIAL)).isSameAs(this.official);

    }

    @Test
    @DisplayName("constructor bir strateji icin politika yoksa IllegalStateException firlatir")
    void shouldThrowIllegalStateException_WhenStrategyHasNoPolicy() {

        assertThatThrownBy(() -> new CountryRankingPolicyRegistry(List.of(this.official)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("WEIGHTED");

    }

    @Test
    @DisplayName("constructor ayni strateji icin iki politika varsa IllegalStateException firlatir")
    void shouldThrowIllegalStateException_WhenStrategyIsRegisteredTwice() {

        assertThatThrownBy(() -> new CountryRankingPolicyRegistry(List.of(this.official, new CountryRankingPolicy(), this.weighted)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("OFFICIAL");

    }

    @Test
    @DisplayName("get() strateji null ise NullPointerException firlatir")
    void shouldThrowNullPointerException_WhenStrategyIsNull() {

        final CountryRankingPolicyRegistry registry = new CountryRankingPolicyRegistry(List.of(this.official, this.weighted));

        assertThatThrownBy(() -> registry.get(null))
                .isInstanceOf(NullPointerException.class);

    }

}
