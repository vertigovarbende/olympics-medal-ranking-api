package com.huawei.olympic_ranking.ranking.model;

import com.huawei.olympic_ranking.common.model.Contender;
import com.huawei.olympic_ranking.ranking.model.policy.RankingPolicy;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public final class Ranking<T extends Contender> {

    private final List<RankedStanding<T>> entries;

    private Ranking(final List<RankedStanding<T>> entries) {
        this.entries = entries;
    }

    public static <T extends Contender> Ranking<T> of(final Collection<Standing<T>> standings, final RankingPolicy policy) {

        Objects.requireNonNull(standings, "standings");
        Objects.requireNonNull(policy, "policy");

        final List<Standing<T>> sorted = standings.stream()
                .filter(Standing::hasMedals)
                .sorted(policy.order())
                .toList();

        final List<RankedStanding<T>> entries = IntStream.range(0, sorted.size())
                .mapToObj(index -> new RankedStanding<>(index + 1, sorted.get(index)))
                .toList();

        return new Ranking<>(entries);
    }

    public List<RankedStanding<T>> entries() {
        return entries;
    }

}
