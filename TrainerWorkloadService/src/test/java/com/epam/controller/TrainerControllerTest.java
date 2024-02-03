package com.epam.controller;

import com.epam.model.dto.TrainerWorkloadDto;
import com.epam.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @Test
    void testSave() {
        TrainerWorkloadDto trainerWorkloadDto = createSampleTrainerWorkloadDto();

        trainerController.save(trainerWorkloadDto);

        verify(trainerService).save(trainerWorkloadDto);
    }

    @Test
    void testGetByFirstnameAndLastname() {
        String firstname = "John";
        String lastname = "Doe";

        trainerController.getByFirstnameAndLastname(firstname, lastname);

        verify(trainerService).findByFirstnameAndLastname(firstname, lastname);
    }

    private TrainerWorkloadDto createSampleTrainerWorkloadDto() {
        TrainerWorkloadDto trainerWorkloadDto = new TrainerWorkloadDto();
        trainerWorkloadDto.setUsername("testUser");

        return trainerWorkloadDto;
    }
}