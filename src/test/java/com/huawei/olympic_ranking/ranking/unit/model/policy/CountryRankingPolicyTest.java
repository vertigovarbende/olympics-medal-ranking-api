package com.huawei.olympic_ranking.ranking.unit.model.policy;

import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.model.policy.CountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder;
import com.huawei.olympic_ranking.testsupport.SampleData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.aCountry;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CountryRankingPolicy - Birim Testleri")
class CountryRankingPolicyTest {

    private final CountryRankingPolicy policy = new CountryRankingPolicy();

    @Test
    @DisplayName("order() once altin sayisina bakar; toplami cok olan ulke bile az altinla geride kalir")
    void shouldRankMoreGoldFirst_EvenWhenFewerTotalMedals() {

        final Standing<Country> moreGold = aCountry("More Gold").withMedals(2, 0, 0).build();
        final Standing<Country> moreTotal = aCountry("More Total").withMedals(1, 5, 5).build();

        final List<String> sorted = sortedNames(List.of(moreTotal, moreGold));

        assertThat(sorted).containsExactly("More Gold", "More Total");

    }

    @Test
    @DisplayName("order() altin esitse gumus sayisi karar verir")
    void shouldRankMoreSilverFirst_WhenGoldIsEqual() {

        final Standing<Country> moreSilver = aCountry("More Silver").withMedals(1, 2, 0).build();
        final Standing<Country> lessSilver = aCountry("Less Silver").withMedals(1, 1, 9).build();

        final List<String> sorted = sortedNames(List.of(lessSilver, moreSilver));

        assertThat(sorted).containsExactly("More Silver", "Less Silver");

    }

    @Test
    @DisplayName("order() altin ve gumus esitse bronz sayisi karar verir")
    void shouldRankMoreBronzeFirst_WhenGoldAndSilverAreEqual() {

        final Standing<Country> moreBronze = aCountry("More Bronze").withMedals(1, 1, 2).build();
        final Standing<Country> lessBronze = aCountry("Less Bronze").withMedals(1, 1, 1).build();

        final List<String> sorted = sortedNames(List.of(lessBronze, moreBronze));

        assertThat(sorted).containsExactly("More Bronze", "Less Bronze");

    }

    @Test
    @DisplayName("order() uc sayi da esitse ulke adini buyuk/kucuk harf duyarsiz alfabetik siralar")
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
    @DisplayName("order() isterin ornek verisinde TUR, USA, JPN sirasini verir")
    void shouldFollowSampleDataOrder_WhenGoldIsEqual() {

        final Standing<Country> turkey = aCountry(SampleData.TUR_NAME).withMedals(1, 1, 0).build();
        final Standing<Country> usa = aCountry(SampleData.USA_NAME).withMedals(1, 0, 1).build();
        final Standing<Country> japan = aCountry(SampleData.JPN_NAME).withMedals(1, 0, 0).build();

        final List<String> sorted = sortedNames(List.of(japan, usa, turkey));

        assertThat(sorted).containsExactly(SampleData.TUR_NAME, SampleData.USA_NAME, SampleData.JPN_NAME);

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
