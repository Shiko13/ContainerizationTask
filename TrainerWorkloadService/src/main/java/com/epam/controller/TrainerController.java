package com.epam.controller;

import com.epam.model.TrainerSummary;
import com.epam.model.dto.TrainerWorkloadDto;
import com.epam.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workload")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping
    public void save(@RequestBody TrainerWorkloadDto trainerWorkloadDto) {
        trainerService.save(trainerWorkloadDto);
    }

    @GetMapping("/search")
    public List<TrainerSummary> getByFirstnameAndLastname(@RequestParam String firstname,
                                                          @RequestParam String lastname) {
        return trainerService.findByFirstnameAndLastname(firstname, lastname);
    }
}
