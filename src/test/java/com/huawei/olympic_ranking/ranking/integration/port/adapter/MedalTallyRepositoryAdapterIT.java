package com.huawei.olympic_ranking.ranking.integration.port.adapter;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.common.model.Contender;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.port.CountryRepository;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import com.huawei.olympic_ranking.ranking.model.MedalTally;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.port.MedalTallyRepository;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.port.SportRepository;
import com.huawei.olympic_ranking.sport.testsupport.SportBuilder;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.SampleDataLoader;
import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MedalTallyRepositoryAdapter - Entegrasyon Testleri")
class MedalTallyRepositoryAdapterIT extends TestContainerConfiguration {

    @Autowired
    private MedalTallyRepository medalTallyRepository;

    @Autowired
    private SampleDataLoader sampleData;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private SportRepository sportRepository;

    @Test
    @DisplayName("findCountryTallies() ornek veride ulke basina altin/gumus/bronz sayilarini doner")
    void shouldReturnCountryTallies_WhenSampleDataLoaded() {

        // Given: isterdeki ornek veri
        this.sampleData.loadAll();

        // When: findCountryTallies
        final List<Standing<Country>> tallies = this.medalTallyRepository.findCountryTallies();

        // Then: her ulkenin sayimi dogrudur
        assertThat(byKey(tallies, Country::getCode)).isEqualTo(Map.of(
                SampleData.TUR, new MedalTally(1, 1, 0),
                SampleData.USA, new MedalTally(1, 0, 1),
                SampleData.JPN, new MedalTally(1, 0, 0)));

    }

    @Test
    @DisplayName("findCountryTallies() madalyasi olmayan ulkeyi dondurmez")
    void shouldExcludeCountry_WhenItHasNoMedals() {

        // Given: ornek veri ve madalyasi olmayan ek bir ulke
        this.sampleData.loadAll();
        this.countryRepository.save(new CountryBuilder().withoutId().withCode("GER").withName("Germany").build());

        // When: findCountryTallies
        final List<Standing<Country>> tallies = this.medalTallyRepository.findCountryTallies();

        // Then: yalnizca madalyali uc ulke doner
        assertThat(byKey(tallies, Country::getCode)).containsOnlyKeys(SampleData.TUR, SampleData.USA, SampleData.JPN);

    }

    @Test
    @DisplayName("findAthleteTallies() sporcu basina sayimlari ve ulke bilgisini doner")
    void shouldReturnAthleteTalliesWithCountry_WhenSampleDataLoaded() {

        // Given: isterdeki ornek veri (Abby Steuter'in madalyasi yok)
        this.sampleData.loadAll();

        // When: findAthleteTallies
        final List<Standing<Athlete>> tallies = this.medalTallyRepository.findAthleteTallies();

        // Then: yalnizca madalyali uc sporcu, dogru sayim ve ulkeyle doner
        assertThat(byKey(tallies, Athlete::getName)).isEqualTo(Map.of(
                SampleData.METE_GAZOZ, new MedalTally(1, 1, 0),
                SampleData.NOAH_LYLES, new MedalTally(1, 0, 1),
                SampleData.HIFUMI_ABE, new MedalTally(1, 0, 0)));

        assertThat(byKey(tallies, Athlete::getName, athlete -> athlete.getCountry().getCode()))
                .isEqualTo(Map.of(
                        SampleData.METE_GAZOZ, SampleData.TUR,
                        SampleData.NOAH_LYLES, SampleData.USA,
                        SampleData.HIFUMI_ABE, SampleData.JPN));

    }

    @Test
    @DisplayName("findSportTallies() spor basina sayimlari doner")
    void shouldReturnSportTallies_WhenSampleDataLoaded() {

        // Given: isterdeki ornek veri
        this.sampleData.loadAll();

        // When: findSportTallies
        final List<Standing<Sport>> tallies = this.medalTallyRepository.findSportTallies();

        // Then: her sporun sayimi dogrudur
        assertThat(byKey(tallies, Sport::getName)).isEqualTo(Map.of(
                SampleData.ARCHERY, new MedalTally(1, 1, 0),
                SampleData.SWIMMING, new MedalTally(1, 0, 1),
                SampleData.JUDO, new MedalTally(1, 0, 0)));

    }

    @Test
    @DisplayName("findSportTallies() madalya verilmemis spor'u dondurmez")
    void shouldExcludeSport_WhenNoMedalWasAwarded() {

        // Given: ornek veri ve madalyasi olmayan ek bir spor
        this.sampleData.loadAll();
        this.sportRepository.save(new SportBuilder().withoutId().withName("Fencing").build());

        // When: findSportTallies
        final List<Standing<Sport>> tallies = this.medalTallyRepository.findSportTallies();

        // Then: yalnizca madalya verilmis uc spor doner
        assertThat(byKey(tallies, Sport::getName))
                .containsOnlyKeys(SampleData.ARCHERY, SampleData.SWIMMING, SampleData.JUDO);

    }

    @Test
    @DisplayName("Sayim sorgulari madalya yokken bos liste doner")
    void shouldReturnEmptyTallies_WhenNoMedalsExist() {

        // Given: bos veritabani
        // When: uc sayim sorgusu
        // Then: hepsi bos
        assertThat(this.medalTallyRepository.findCountryTallies()).isEmpty();
        assertThat(this.medalTallyRepository.findAthleteTallies()).isEmpty();
        assertThat(this.medalTallyRepository.findSportTallies()).isEmpty();

    }

    private static <T extends Contender> Map<String, MedalTally> byKey(
            final List<Standing<T>> tallies, final Function<T, String> key) {
        return tallies.stream().collect(Collectors.toMap(standing -> key.apply(standing.contender()), Standing::tally));
    }

    private static <T extends Contender> Map<String, String> byKey(
            final List<Standing<T>> tallies, final Function<T, String> key, final Function<T, String> value) {
        return tallies.stream().collect(Collectors.toMap(
                standing -> key.apply(standing.contender()), standing -> value.apply(standing.contender())));
    }

}
