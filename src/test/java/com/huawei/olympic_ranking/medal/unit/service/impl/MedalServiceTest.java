package com.huawei.olympic_ranking.medal.unit.service.impl;

import com.huawei.olympic_ranking.athlete.exception.AthleteNotFoundException;
import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.port.AthleteRepository;
import com.huawei.olympic_ranking.athlete.testsupport.AthleteBuilder;
import com.huawei.olympic_ranking.medal.exception.DuplicateMedalException;
import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.medal.port.MedalRepository;
import com.huawei.olympic_ranking.medal.service.impl.MedalService;
import com.huawei.olympic_ranking.medal.testsupport.MedalBuilder;
import com.huawei.olympic_ranking.sport.exception.SportNotFoundException;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.port.SportRepository;
import com.huawei.olympic_ranking.sport.testsupport.SportBuilder;
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
@DisplayName("MedalService - Birim Testleri")
class MedalServiceTest {

    private static final Long ATHLETE_ID = 1L;
    private static final Long SPORT_ID = 2L;

    private static final Athlete ATHLETE = new AthleteBuilder().withId(ATHLETE_ID).build();
    private static final Sport SPORT = new SportBuilder().withId(SPORT_ID).build();

    @Mock
    private MedalRepository medalRepository;

    @Mock
    private AthleteRepository athleteRepository;

    @Mock
    private SportRepository sportRepository;

    @InjectMocks
    private MedalService medalService;

    @Test
    @DisplayName("create() sporcu ve spor varsa madalyayi kaydeder ve kaydedileni dondurur")
    void shouldSaveMedal_WhenValidInput() {

        final Medal saved = new MedalBuilder().withId(10L).withAthlete(ATHLETE).withSport(SPORT).withType(MedalType.GOLD).build();
        when(this.athleteRepository.findById(ATHLETE_ID)).thenReturn(Optional.of(ATHLETE));
        when(this.sportRepository.findById(SPORT_ID)).thenReturn(Optional.of(SPORT));
        when(this.medalRepository.existsByAthleteIdAndSportIdAndType(ATHLETE_ID, SPORT_ID, MedalType.GOLD)).thenReturn(false);
        when(this.medalRepository.save(any(Medal.class))).thenReturn(saved);

        final Medal result = this.medalService.create(ATHLETE_ID, SPORT_ID, MedalType.GOLD);

        assertThat(result).isSameAs(saved);

        final ArgumentCaptor<Medal> captor = ArgumentCaptor.forClass(Medal.class);
        verify(this.medalRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getAthlete()).isSameAs(ATHLETE);
        assertThat(captor.getValue().getSport()).isSameAs(SPORT);
        assertThat(captor.getValue().getType()).isEqualTo(MedalType.GOLD);

    }

    @Test
    @DisplayName("create() ayni sporcu ve spor icin farkli madalya turunu kabul eder")
    void shouldSaveMedal_WhenSameAthleteAndSportButDifferentType() {

        when(this.athleteRepository.findById(ATHLETE_ID)).thenReturn(Optional.of(ATHLETE));
        when(this.sportRepository.findById(SPORT_ID)).thenReturn(Optional.of(SPORT));
        when(this.medalRepository.existsByAthleteIdAndSportIdAndType(ATHLETE_ID, SPORT_ID, MedalType.SILVER)).thenReturn(false);
        when(this.medalRepository.save(any(Medal.class))).thenReturn(new MedalBuilder().withType(MedalType.SILVER).build());

        this.medalService.create(ATHLETE_ID, SPORT_ID, MedalType.SILVER);

        verify(this.medalRepository).existsByAthleteIdAndSportIdAndType(ATHLETE_ID, SPORT_ID, MedalType.SILVER);
        verify(this.medalRepository).save(any(Medal.class));

    }

    @Test
    @DisplayName("create() ayni sporcu, spor ve tur zaten varsa DuplicateMedalException firlatir ve kaydetmez")
    void shouldThrowException_WhenDuplicateMedal() {

        when(this.athleteRepository.findById(ATHLETE_ID)).thenReturn(Optional.of(ATHLETE));
        when(this.sportRepository.findById(SPORT_ID)).thenReturn(Optional.of(SPORT));
        when(this.medalRepository.existsByAthleteIdAndSportIdAndType(ATHLETE_ID, SPORT_ID, MedalType.GOLD)).thenReturn(true);

        assertThatThrownBy(() -> this.medalService.create(ATHLETE_ID, SPORT_ID, MedalType.GOLD))
                .isInstanceOf(DuplicateMedalException.class);

        verify(this.medalRepository, never()).save(any());

    }

    @Test
    @DisplayName("create() sporcu bulunamazsa AthleteNotFoundException firlatir ve baska sorgu yapmaz")
    void shouldThrowAthleteNotFoundException_WhenAthleteDoesNotExist() {

        when(this.athleteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.medalService.create(99L, SPORT_ID, MedalType.GOLD))
                .isInstanceOf(AthleteNotFoundException.class);

        verifyNoInteractions(this.sportRepository, this.medalRepository);

    }

    @Test
    @DisplayName("create() spor bulunamazsa SportNotFoundException firlatir ve madalya repository'sine dokunmaz")
    void shouldThrowSportNotFoundException_WhenSportDoesNotExist() {

        when(this.athleteRepository.findById(ATHLETE_ID)).thenReturn(Optional.of(ATHLETE));
        when(this.sportRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.medalService.create(ATHLETE_ID, 99L, MedalType.GOLD))
                .isInstanceOf(SportNotFoundException.class);

        verifyNoInteractions(this.medalRepository);

    }

}
