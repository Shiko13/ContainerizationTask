package com.epam.service;

import com.epam.model.TrainerSummary;
import com.epam.model.TrainingMonthSummary;
import com.epam.model.TrainingYearSummary;
import com.epam.model.dto.ActionType;
import com.epam.model.dto.TrainerWorkloadDto;
import com.epam.repo.TrainerSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerSummaryRepository trainerSummaryRepository;

    @Override
    @Transactional
    public void save(TrainerWorkloadDto trainerWorkloadDto) {
        log.info("save, trainerWorkloadDto = {}", trainerWorkloadDto);

        if (trainerWorkloadDto.getActionType() == ActionType.DELETE) {
            trainerWorkloadDto.setDuration(-trainerWorkloadDto.getDuration());
        }

        String trainerId = trainerWorkloadDto.getUsername();
        TrainerSummary trainerSummary = trainerSummaryRepository.findByTrainerId(trainerId);

        if (trainerSummary != null) {
            updateTrainerSummary(trainerSummary, trainerWorkloadDto);
        } else {
            trainerSummary = createTrainerSummary(trainerWorkloadDto);
        }

        trainerSummaryRepository.save(trainerSummary);
    }

    @Override
    public List<TrainerSummary> findByFirstnameAndLastname(String firstname, String lastname) {
        log.info("trainerSummaryRepository, firstname = {}, lastname = {}", firstname, lastname);

        return trainerSummaryRepository.findByFirstNameAndLastName(firstname, lastname);
    }

    public void updateTrainerSummary(TrainerSummary trainerSummary, TrainerWorkloadDto trainerWorkloadDto) {
        int year = trainerWorkloadDto.getDate().getYear();
        int month = trainerWorkloadDto.getDate().getMonthValue();

        TrainingYearSummary yearSummary = trainerSummary.getTrainingSummaries()
                                                        .stream()
                                                        .filter(y -> y.getYear() == year)
                                                        .findFirst()
                                                        .orElse(new TrainingYearSummary(year, new ArrayList<>()));

        TrainingMonthSummary monthSummary = yearSummary.getMonths()
                                                       .stream()
                                                       .filter(m -> m.getMonth() == month)
                                                       .findFirst()
                                                       .orElse(new TrainingMonthSummary(month, 0L));

        monthSummary.setDuration(monthSummary.getDuration() + trainerWorkloadDto.getDuration());

        if (!yearSummary.getMonths().contains(monthSummary)) {
            yearSummary.getMonths().add(monthSummary);
        }

        if (!trainerSummary.getTrainingSummaries().contains(yearSummary)) {
            trainerSummary.getTrainingSummaries().add(yearSummary);
        }
    }

    private TrainerSummary createTrainerSummary(TrainerWorkloadDto trainerWorkloadDto) {
        TrainerSummary trainerSummary = new TrainerSummary();
        trainerSummary.setTrainerId(trainerWorkloadDto.getUsername());
        trainerSummary.setFirstName(trainerWorkloadDto.getFirstName());
        trainerSummary.setLastName(trainerWorkloadDto.getLastName());
        trainerSummary.setIsActive(trainerWorkloadDto.getIsActive());

        TrainingYearSummary trainingYearSummary = new TrainingYearSummary();
        trainingYearSummary.setYear(trainerWorkloadDto.getDate().getYear());
        trainingYearSummary.setMonths(new ArrayList<>());

        TrainingMonthSummary trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonth(trainerWorkloadDto.getDate().getMonthValue());
        trainingMonthSummary.setDuration(trainerWorkloadDto.getDuration());

        trainingYearSummary.getMonths().add(trainingMonthSummary);
        trainerSummary.setTrainingSummaries(Collections.singletonList(trainingYearSummary));

        return trainerSummary;
    }
}