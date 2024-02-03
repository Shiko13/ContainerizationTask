package com.epam.repo;

import com.epam.model.TrainerSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrainerSummaryRepository extends MongoRepository<TrainerSummary, String> {

    TrainerSummary findByTrainerId(String trainerId);

    List<TrainerSummary> findByFirstNameAndLastName(String firstName, String lastName);
}
