package com.huawei.olympic_ranking.country.integration.port.adapter;

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

@DisplayName("CountryRepositoryAdapter - Entegrasyon Testleri")
class CountryRepositoryAdapterIT extends TestContainerConfiguration {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    @DisplayName("save() yeni ulkeyi kaydeder ve id atar")
    void shouldSaveCountry_WhenNewCountry() {

        // Given: id'si olmayan ulke
        final Country country = new CountryBuilder().withoutId().build();

        // When: save
        final Country saved = this.countryRepository.save(country);

        // Then: id atanir, kod ve ad korunur
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCode()).isEqualTo(SampleData.TUR);
        assertThat(saved.getName()).isEqualTo(SampleData.TUR_NAME);

    }

    @Test
    @DisplayName("save() ayni kodlu ikinci ulkede veri butunlugu hatasi firlatir")
    void shouldThrowException_WhenCodeIsDuplicated() {

        // Given: TUR kodlu ulke kayitli
        this.countryRepository.save(new CountryBuilder().withoutId().build());
        final Country duplicate = new CountryBuilder().withoutId().withName("Baska Ad").build();

        // When: ayni kodla ikinci kayit
        // Then: benzersizlik kisiti ihlal edilir
        assertThatThrownBy(() -> this.countryRepository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("findByCode() kayitli ulkeyi kodla bulur")
    void shouldFindCountry_WhenCodeExists() {

        // Given: kayitli ulke
        final Country saved = this.countryRepository.save(new CountryBuilder().withoutId().build());

        // When: findByCode
        final Optional<Country> found = this.countryRepository.findByCode(SampleData.TUR);

        // Then: ayni ulke doner
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getName()).isEqualTo(SampleData.TUR_NAME);

    }

    @Test
    @DisplayName("findByCode() kayit yoksa bos doner")
    void shouldReturnEmpty_WhenCodeDoesNotExist() {

        // Given: bos veritabani
        // When: findByCode
        final Optional<Country> found = this.countryRepository.findByCode(SampleData.TUR);

        // Then: sonuc bos
        assertThat(found).isEmpty();

    }

    @Test
    @DisplayName("existsByCode() kayitli kod icin true doner")
    void shouldReturnTrue_WhenCodeExists() {

        // Given: kayitli ulke
        this.countryRepository.save(new CountryBuilder().withoutId().build());

        // When: existsByCode
        final boolean exists = this.countryRepository.existsByCode(SampleData.TUR);

        // Then: true
        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("existsByCode() kayitsiz kod icin false doner")
    void shouldReturnFalse_WhenCodeDoesNotExist() {

        // Given: yalnizca TUR kayitli
        this.countryRepository.save(new CountryBuilder().withoutId().build());

        // When: baska kodla existsByCode
        final boolean exists = this.countryRepository.existsByCode(SampleData.USA);

        // Then: false
        assertThat(exists).isFalse();

    }

}
