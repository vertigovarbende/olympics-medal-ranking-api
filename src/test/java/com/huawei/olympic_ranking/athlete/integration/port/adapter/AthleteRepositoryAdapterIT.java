package com.huawei.olympic_ranking.athlete.integration.port.adapter;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.port.AthleteRepository;
import com.huawei.olympic_ranking.athlete.testsupport.AthleteBuilder;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.port.CountryRepository;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AthleteRepositoryAdapter - Entegrasyon Testleri")
class AthleteRepositoryAdapterIT extends TestContainerConfiguration {

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    @DisplayName("save() sporcuyu kayitli ulkesiyle kaydeder ve id atar")
    void shouldSaveAthlete_WhenCountryExists() {

        // Given: kayitli ulke ve id'siz sporcu
        final Country country = this.savedCountry();
        final Athlete athlete = new AthleteBuilder().withoutId().withCountry(country).build();

        // When: save
        final Athlete saved = this.athleteRepository.save(athlete);

        // Then: id atanir, ad ve ulke korunur
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(SampleData.METE_GAZOZ);
        assertThat(saved.getCountry().getId()).isEqualTo(country.getId());

    }

    @Test
    @DisplayName("save() olmayan ulke icin veri butunlugu hatasi firlatir")
    void shouldThrowException_WhenCountryDoesNotExist() {

        // Given: veritabaninda olmayan ulkeye bagli sporcu
        final Country missingCountry = new CountryBuilder().withId(999_999L).build();
        final Athlete athlete = new AthleteBuilder().withoutId().withCountry(missingCountry).build();

        // When: save
        // Then: yabanci anahtar kisiti ihlal edilir
        assertThatThrownBy(() -> this.athleteRepository.save(athlete))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("findById() sporcuyu ulke bilgisiyle birlikte getirir")
    void shouldFindAthleteWithCountry_WhenIdExists() {

        // Given: kayitli sporcu
        final Country country = this.savedCountry();
        final Athlete saved = this.athleteRepository.save(new AthleteBuilder().withoutId().withCountry(country).build());

        // When: findById
        final Optional<Athlete> found = this.athleteRepository.findById(saved.getId());

        // Then: sporcu ve tembel yuklenen ulke bilgisi doner
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(SampleData.METE_GAZOZ);
        assertThat(found.get().getCountry().getCode()).isEqualTo(SampleData.TUR);
        assertThat(found.get().getCountry().getName()).isEqualTo(SampleData.TUR_NAME);

    }

    @Test
    @DisplayName("findById() kayit yoksa bos doner")
    void shouldReturnEmpty_WhenIdDoesNotExist() {

        // Given: bos veritabani
        // When: findById
        final Optional<Athlete> found = this.athleteRepository.findById(999_999L);

        // Then: sonuc bos
        assertThat(found).isEmpty();

    }

    private Country savedCountry() {
        return this.countryRepository.save(new CountryBuilder().withoutId().build());
    }

}
