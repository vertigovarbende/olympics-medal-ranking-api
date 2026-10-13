package com.huawei.olympic_ranking.athlete.unit.service.impl;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.port.AthleteRepository;
import com.huawei.olympic_ranking.athlete.service.impl.AthleteService;
import com.huawei.olympic_ranking.athlete.testsupport.AthleteBuilder;
import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.country.exception.CountryNotFoundException;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.port.CountryRepository;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AthleteService - Birim Testleri")
class AthleteServiceTest {

    private static final Country TURKEY = new CountryBuilder().build();

    @Mock
    private AthleteRepository athleteRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private AthleteService athleteService;

    @Test
    @DisplayName("create() ulke varsa sporcuyu o ulkeyle kaydeder ve kaydedileni dondurur")
    void shouldSaveAthlete_WhenCountryExists() {

        final Athlete saved = new AthleteBuilder().withId(1L).withName("Mete Gazoz").withCountry(TURKEY).build();
        when(this.countryRepository.findByCode("TUR")).thenReturn(Optional.of(TURKEY));
        when(this.athleteRepository.save(any(Athlete.class))).thenReturn(saved);

        final Athlete result = this.athleteService.create("Mete Gazoz", "TUR");

        assertThat(result).isSameAs(saved);

        final ArgumentCaptor<Athlete> captor = ArgumentCaptor.forClass(Athlete.class);
        verify(this.athleteRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getName()).isEqualTo("Mete Gazoz");
        assertThat(captor.getValue().getCountry()).isSameAs(TURKEY);

    }

    @Test
    @DisplayName("create() ulkeyi normalize edilmis kodla arar")
    void shouldResolveCountryWithNormalizedCode_WhenCodeIsLowerCase() {

        when(this.countryRepository.findByCode("TUR")).thenReturn(Optional.of(TURKEY));
        when(this.athleteRepository.save(any(Athlete.class))).thenReturn(new AthleteBuilder().build());

        this.athleteService.create("Mete Gazoz", " tur ");

        verify(this.countryRepository).findByCode("TUR");

    }

    @Test
    @DisplayName("create() ulke bulunamazsa CountryNotFoundException firlatir ve kaydetmez")
    void shouldThrowCountryNotFoundException_WhenCountryDoesNotExist() {

        when(this.countryRepository.findByCode("XXX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.athleteService.create("Mete Gazoz", "XXX"))
                .isInstanceOf(CountryNotFoundException.class);

        verify(this.athleteRepository, never()).save(any());

    }

    @Test
    @DisplayName("create() ad bossa DomainValidationException firlatir ve kaydetmez")
    void shouldThrowValidationException_WhenNameIsBlank() {

        when(this.countryRepository.findByCode("TUR")).thenReturn(Optional.of(TURKEY));

        assertThatThrownBy(() -> this.athleteService.create(" ", "TUR"))
                .isInstanceOf(DomainValidationException.class);

        verify(this.athleteRepository, never()).save(any());

    }

    @Test
    @DisplayName("create() ulke kodu bossa DomainValidationException firlatir ve repository'lere hic dokunmaz")
    void shouldThrowValidationException_WhenCountryCodeIsBlank() {

        assertThatThrownBy(() -> this.athleteService.create("Mete Gazoz", " "))
                .isInstanceOf(DomainValidationException.class);

        verifyNoInteractions(this.countryRepository, this.athleteRepository);

    }

}
