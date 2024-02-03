package com.epam.service;

import com.epam.model.TrainerSummary;
import com.epam.model.dto.TrainerWorkloadDto;

import java.util.List;

public interface TrainerService {

    void save(TrainerWorkloadDto trainerWorkloadDto);

    List<TrainerSummary> findByFirstnameAndLastname(String firstname, String lastname);
}
