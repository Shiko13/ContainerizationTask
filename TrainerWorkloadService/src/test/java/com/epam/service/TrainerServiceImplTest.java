package com.epam.service;

import com.epam.model.TrainerSummary;
import com.epam.model.TrainingMonthSummary;
import com.epam.model.TrainingYearSummary;
import com.epam.model.dto.ActionType;
import com.epam.model.dto.TrainerWorkloadDto;
import com.epam.repo.TrainerSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @Mock
    private TrainerSummaryRepository trainerSummaryRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void save_CreateNewTrainerSummary() {
        TrainerWorkloadDto workloadDto = createSampleWorkloadDto(ActionType.POST);
        when(trainerSummaryRepository.findByTrainerId(anyString())).thenReturn(null);

        trainerService.save(workloadDto);

        verify(trainerSummaryRepository).save(any());
    }

    @Test
    void save_DeleteActionType_ShouldSetNegativeDuration() {
        TrainerWorkloadDto workloadDto = new TrainerWorkloadDto();
        workloadDto.setActionType(ActionType.DELETE);
        workloadDto.setDuration(100L);
        workloadDto.setUsername("Username");
        workloadDto.setDate(LocalDate.now());

        when(trainerSummaryRepository.findByTrainerId("Username")).thenReturn(null);

        trainerService.save(workloadDto);

        assertEquals(-100L, workloadDto.getDuration());
    }

    @Test
    void updateTrainerSummary_ShouldUpdateExistingTrainerSummary() {
        TrainerWorkloadDto workloadDto = new TrainerWorkloadDto();
        TrainerSummary existingTrainerSummary = new TrainerSummary();
        existingTrainerSummary.setTrainingSummaries(new ArrayList<>());

        int year = LocalDate.now().getYear();
        workloadDto.setDate(LocalDate.now());
        workloadDto.setDuration(100L);

        TrainingMonthSummary mockMonthSummary = mock(TrainingMonthSummary.class);
        when(mockMonthSummary.getDuration()).thenReturn(100L);

        TrainingYearSummary mockYearSummary = mock(TrainingYearSummary.class);
        mockYearSummary.setYear(year);
        mockYearSummary.setMonths(new ArrayList<>());
        mockYearSummary.getMonths().add(mockMonthSummary);

        existingTrainerSummary.getTrainingSummaries().add(mockYearSummary);

        trainerService.updateTrainerSummary(existingTrainerSummary, workloadDto);

        assertEquals(100L, mockMonthSummary.getDuration());
    }


    public static TrainerWorkloadDto createSampleWorkloadDto(ActionType actionType) {
        TrainerWorkloadDto workloadDto = new TrainerWorkloadDto();
        workloadDto.setUsername("sampleUsername");
        workloadDto.setFirstName("John");
        workloadDto.setLastName("Doe");
        workloadDto.setIsActive(true);
        workloadDto.setActionType(actionType);
        workloadDto.setDate(LocalDate.now());
        workloadDto.setDuration(120L);

        return workloadDto;
    }
}
