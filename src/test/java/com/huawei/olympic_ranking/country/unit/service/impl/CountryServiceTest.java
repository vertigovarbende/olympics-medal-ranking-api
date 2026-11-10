package com.huawei.olympic_ranking.country.unit.service.impl;

import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.country.exception.DuplicateCountryException;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.port.CountryRepository;
import com.huawei.olympic_ranking.country.service.impl.CountryService;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CountryService - Birim Testleri")
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    @DisplayName("create() kod daha once kullanilmadiysa ulkeyi kaydeder ve kaydedileni dondurur")
    void shouldSaveCountry_WhenValidInput() {

        final Country saved = new CountryBuilder().withId(1L).withCode("TUR").withName("Türkiye").build();
        when(this.countryRepository.existsByCode("TUR")).thenReturn(false);
        when(this.countryRepository.save(any(Country.class))).thenReturn(saved);

        final Country result = this.countryService.create("Türkiye", "TUR");

        assertThat(result).isSameAs(saved);

        final ArgumentCaptor<Country> captor = ArgumentCaptor.forClass(Country.class);
        verify(this.countryRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getCode()).isEqualTo("TUR");
        assertThat(captor.getValue().getName()).isEqualTo("Türkiye");

    }

    @Test
    @DisplayName("create() tekrar kontrolunu normalize edilmis kodla yapar")
    void shouldCheckDuplicateWithNormalizedCode_WhenCodeIsLowerCase() {

        when(this.countryRepository.existsByCode("TUR")).thenReturn(false);
        when(this.countryRepository.save(any(Country.class))).thenReturn(new CountryBuilder().build());

        this.countryService.create("Türkiye", " tur ");

        verify(this.countryRepository).existsByCode("TUR");

    }

    @Test
    @DisplayName("create() kod zaten varsa DuplicateCountryException firlatir ve kaydetmez")
    void shouldThrowDuplicateCountryException_WhenCodeAlreadyExists() {

        when(this.countryRepository.existsByCode("TUR")).thenReturn(true);

        assertThatThrownBy(() -> this.countryService.create("Türkiye", "TUR"))
                .isInstanceOf(DuplicateCountryException.class);

        verify(this.countryRepository, never()).save(any());

    }

    @Test
    @DisplayName("create() kod gecersizse DomainValidationException firlatir ve repository'ye hic dokunmaz")
    void shouldThrowValidationException_WhenCodeIsInvalid() {

        assertThatThrownBy(() -> this.countryService.create("Türkiye", "TURK"))
                .isInstanceOf(DomainValidationException.class);

        verifyNoInteractions(this.countryRepository);

    }

}
