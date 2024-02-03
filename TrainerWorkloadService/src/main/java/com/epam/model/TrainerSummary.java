package com.epam.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "trainer_summary")
@CompoundIndex(name = "name_Index", def = "{'lastName': 1, 'firstName': 1}")
public class TrainerSummary {

    @Id
    private String id;
    private String trainerId; //Username
    private String firstName;
    private String lastName;
    private Boolean isActive;

    private List<TrainingYearSummary> trainingSummaries;
}
