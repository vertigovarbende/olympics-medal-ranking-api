package com.huawei.olympic_ranking.ranking.unit.model.policy;

import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import com.huawei.olympic_ranking.ranking.model.RankingStrategy;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.model.policy.WeightedCountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder;
import com.huawei.olympic_ranking.testsupport.SampleData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.aCountry;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WeightedCountryRankingPolicy - Birim Testleri")
class WeightedCountryRankingPolicyTest {

    private final WeightedCountryRankingPolicy policy = new WeightedCountryRankingPolicy();

    @Test
    @DisplayName("strategy() WEIGHTED doner")
    void shouldReturnWeightedStrategy() {

        assertThat(this.policy.strategy()).isEqualTo(RankingStrategy.WEIGHTED);

    }

    @Test
    @DisplayName("order() once agirlikli puana bakar; daha az altinli ama daha cok puanli ulke onde gelir")
    void shouldRankMorePointsFirst_EvenWhenFewerGold() {

        final Standing<Country> moreGold = aCountry("More Gold").withMedals(2, 0, 0).build();
        final Standing<Country> morePoints = aCountry("More Points").withMedals(1, 5, 5).build();

        final List<String> sorted = sortedNames(List.of(moreGold, morePoints));

        assertThat(sorted).containsExactly("More Points", "More Gold");

    }

    @Test
    @DisplayName("order() puan esitse altin sayisi karar verir")
    void shouldRankMoreGoldFirst_WhenPointsAreEqual() {

        final Standing<Country> oneGold = aCountry("One Gold").withMedals(1, 0, 0).build();
        final Standing<Country> silverAndBronze = aCountry("Silver And Bronze").withMedals(0, 1, 1).build();

        final List<String> sorted = sortedNames(List.of(silverAndBronze, oneGold));

        assertThat(sorted).containsExactly("One Gold", "Silver And Bronze");

    }

    @Test
    @DisplayName("order() puan ve altin esitse gumus sayisi karar verir")
    void shouldRankMoreSilverFirst_WhenPointsAndGoldAreEqual() {

        final Standing<Country> moreSilver = aCountry("More Silver").withMedals(1, 2, 0).build();
        final Standing<Country> moreBronze = aCountry("More Bronze").withMedals(1, 1, 2).build();

        final List<String> sorted = sortedNames(List.of(moreBronze, moreSilver));

        assertThat(sorted).containsExactly("More Silver", "More Bronze");

    }

    @Test
    @DisplayName("order() madalya sayilari esitse ulke adini buyuk/kucuk harf duyarsiz alfabetik siralar")
    void shouldRankByNameIgnoringCase_WhenAllMedalsAreEqual() {

        final Standing<Country> lowerCase = aCountry("brazil").withMedals(1, 1, 1).build();
        final Standing<Country> upperCase = aCountry("Algeria").withMedals(1, 1, 1).build();

        final List<String> sorted = sortedNames(List.of(lowerCase, upperCase));

        assertThat(sorted).containsExactly("Algeria", "brazil");

    }

    @Test
    @DisplayName("order() ad da esitse id'si kucuk olani once siralar")
    void shouldRankByIdAscending_WhenNameAndMedalsAreEqual() {

        final Country firstCountry = new CountryBuilder().withId(1L).withCode("AAA").withName("Same").build();
        final Country secondCountry = new CountryBuilder().withId(2L).withCode("BBB").withName("Same").build();
        final Standing<Country> first = StandingBuilder.of(firstCountry).withMedals(1, 0, 0).build();
        final Standing<Country> second = StandingBuilder.of(secondCountry).withMedals(1, 0, 0).build();

        final List<Standing<Country>> sorted = sort(List.of(second, first));

        assertThat(sorted).extracting(Standing::id).containsExactly(1L, 2L);

    }

    @Test
    @DisplayName("order() USA (2G, 1S) ve TUR (1G, 3S) icin resmi kuralin tersine TUR'u one alir")
    void shouldRankTurkeyFirst_WhenUsaHasMoreGoldButFewerPoints() {

        final Standing<Country> usa = aCountry(SampleData.USA_NAME).withMedals(2, 1, 0).build();
        final Standing<Country> turkey = aCountry(SampleData.TUR_NAME).withMedals(1, 3, 0).build();

        final List<String> sorted = sortedNames(List.of(usa, turkey));

        assertThat(sorted).containsExactly(SampleData.TUR_NAME, SampleData.USA_NAME);

    }

    private List<Standing<Country>> sort(final List<Standing<Country>> standings) {

        final List<Standing<Country>> list = new ArrayList<>(standings);
        list.sort(this.policy.order());

        return list;
    }

    private List<String> sortedNames(final List<Standing<Country>> standings) {
        return sort(standings).stream().map(Standing::name).toList();
    }

}
