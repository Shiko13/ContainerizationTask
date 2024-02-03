package com.epam.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainerWorkloadDto {

    private String username;

    private String firstName;

    private String lastName;

    private Boolean isActive;

    private LocalDate date;

    private Long duration;

    private ActionType actionType;
}
