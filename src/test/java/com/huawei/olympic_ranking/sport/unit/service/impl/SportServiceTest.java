package com.huawei.olympic_ranking.sport.unit.service.impl;

import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.sport.exception.DuplicateSportException;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.port.SportRepository;
import com.huawei.olympic_ranking.sport.service.impl.SportService;
import com.huawei.olympic_ranking.sport.testsupport.SportBuilder;
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
@DisplayName("SportService - Birim Testleri")
class SportServiceTest {

    @Mock
    private SportRepository sportRepository;

    @InjectMocks
    private SportService sportService;

    @Test
    @DisplayName("create() ad daha once kullanilmadiysa spor'u kaydeder ve kaydedileni dondurur")
    void shouldSaveSport_WhenValidInput() {

        final Sport saved = new SportBuilder().withId(1L).withName("Archery").build();
        when(this.sportRepository.existsByNameIgnoreCase("Archery")).thenReturn(false);
        when(this.sportRepository.save(any(Sport.class))).thenReturn(saved);

        final Sport result = this.sportService.create("Archery");

        assertThat(result).isSameAs(saved);

        final ArgumentCaptor<Sport> captor = ArgumentCaptor.forClass(Sport.class);
        verify(this.sportRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getName()).isEqualTo("Archery");

    }

    @Test
    @DisplayName("create() tekrar kontrolunu kirpilmis adla yapar")
    void shouldCheckDuplicateWithStrippedName_WhenNameHasSurroundingSpaces() {

        when(this.sportRepository.existsByNameIgnoreCase("Judo")).thenReturn(false);
        when(this.sportRepository.save(any(Sport.class))).thenReturn(new SportBuilder().withName("Judo").build());

        this.sportService.create("  Judo ");

        verify(this.sportRepository).existsByNameIgnoreCase("Judo");

    }

    @Test
    @DisplayName("create() ad zaten varsa DuplicateSportException firlatir ve kaydetmez")
    void shouldThrowDuplicateSportException_WhenNameAlreadyExists() {

        when(this.sportRepository.existsByNameIgnoreCase("Archery")).thenReturn(true);

        assertThatThrownBy(() -> this.sportService.create("Archery"))
                .isInstanceOf(DuplicateSportException.class);

        verify(this.sportRepository, never()).save(any());

    }

    @Test
    @DisplayName("create() ad bossa DomainValidationException firlatir ve repository'ye hic dokunmaz")
    void shouldThrowValidationException_WhenNameIsBlank() {

        assertThatThrownBy(() -> this.sportService.create(" "))
                .isInstanceOf(DomainValidationException.class);

        verifyNoInteractions(this.sportRepository);

    }

}
