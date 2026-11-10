package com.huawei.olympic_ranking.ranking.model.policy;

import com.huawei.olympic_ranking.ranking.model.MedalTally;
import com.huawei.olympic_ranking.ranking.model.Standing;

import java.util.Comparator;
import java.util.function.ToLongFunction;

final class StandingOrders {

    static final Comparator<Standing<?>> MOST_MEDALS_FIRST = Comparator.comparingLong((Standing<?> standing) -> standing.tally().total()).reversed();

    static final Comparator<Standing<?>> BY_NAME_THEN_ID = Comparator.comparing((Standing<?> standing) -> standing.name(), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing((Standing<?> standing) -> standing.name())
                    .thenComparing((Standing<?> standing) -> standing.id(), Comparator.nullsLast(Comparator.<Long>naturalOrder()));

    private StandingOrders() {

    }

    static Comparator<Standing<?>> most(final ToLongFunction<MedalTally> medals) {
        return Comparator.comparingLong((Standing<?> standing) -> medals.applyAsLong(standing.tally())).reversed();
    }

}
