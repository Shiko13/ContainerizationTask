package com.epam.service;

import com.epam.aspect.TransactionLoggingAspect;
import com.epam.error.AccessException;
import com.epam.error.ErrorMessageConstants;
import com.epam.mapper.TrainingMapper;
import com.epam.model.Training;
import com.epam.model.dto.ActionType;
import com.epam.model.dto.TrainerWorkloadDto;
import com.epam.model.dto.TrainingDtoInput;
import com.epam.model.dto.TrainingForTraineeDtoOutput;
import com.epam.model.dto.TrainingForTrainerDtoOutput;
import com.epam.repo.TraineeRepo;
import com.epam.repo.TrainerRepo;
import com.epam.repo.TrainingRepo;
import com.epam.spec.TrainingTraineeSpecification;
import com.epam.spec.TrainingTrainerSpecification;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepo trainingRepo;

    private final TraineeRepo traineeRepo;

    private final TrainerRepo trainerRepo;

    private final TrainingMapper trainingMapper;

    private final JmsTemplate jmsTemplate;

    @Override
    @Transactional
    public Training save(TrainingDtoInput trainingDtoInput, HttpServletRequest request) {
        log.info("save, trainingDtoInput = {}", trainingDtoInput);

        var trainingToSave = trainingMapper.toEntity(trainingDtoInput);
        var trainee = traineeRepo.findByUser_Username(trainingDtoInput.getTraineeUsername())
                                 .orElseThrow(() -> new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE));
        var trainer = trainerRepo.findByUser_Username(trainingDtoInput.getTrainerUsername())
                                 .orElseThrow(() -> new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE));

        trainingToSave.setTrainee(trainee);
        trainingToSave.setTrainer(trainer);

        var savedTraining = trainingRepo.save(trainingToSave);

        sendRequest(savedTraining, ActionType.POST);

        return savedTraining;
    }

    @Override
    @Transactional
    @Timed("findByDateRangeAndTraineeUsernameTime")
    public List<TrainingForTraineeDtoOutput> findByDateRangeAndTraineeUsername(
            TrainingTraineeSpecification specification) {

        log.info("findByDateRangeAndTraineeUsername, specification = {}", specification);

        var trainings = trainingRepo.findAll(specification);

        return trainingMapper.toTrainingForTraineeDtoList(trainings);
    }

    @Override
    @Transactional
    @Timed("findByDateRangeAndTrainerUsernameTime")
    public List<TrainingForTrainerDtoOutput> findByDateRangeAndTrainerUsername(
            TrainingTrainerSpecification specification) {
        log.info("findByDateRangeAndTrainerUsername, specification = {}", specification);

        var trainings = trainingRepo.findAll(specification);

        return trainingMapper.toTrainingForTrainerDtoList(trainings);
    }

    @Override
    @Transactional
    public void deleteById(Long id, HttpServletRequest request) {
        log.info("deleteById, id = {}", id);

        var training = getTrainingById(id);

        trainingRepo.delete(training);

        sendRequest(training, ActionType.DELETE);
    }

    private Training getTrainingById(Long id) {
        return trainingRepo.findById(id)
                           .orElseThrow(() -> new AccessException(ErrorMessageConstants.NOT_FOUND_MESSAGE));
    }

    public void sendRequest(Training training, ActionType actionType) {
        TrainerWorkloadDto trainerDto = createTrainerDto(training, actionType);
        String transactionId = MDC.get(TransactionLoggingAspect.TRANSACTION_ID_KEY);

        jmsTemplate.send("trainer-workload-queue", session -> {
            Message message = Objects.requireNonNull(jmsTemplate.getMessageConverter()).toMessage(trainerDto, session);
            message.setStringProperty(TransactionLoggingAspect.TRANSACTION_ID_KEY, transactionId);
            return message;
        });
    }

    private TrainerWorkloadDto createTrainerDto(Training training, ActionType actionType) {
        return TrainerWorkloadDto.builder()
                                 .username(training.getTrainer().getUser().getUsername())
                                 .firstName(training.getTrainer().getUser().getFirstName())
                                 .lastName(training.getTrainer().getUser().getLastName())
                                 .isActive(training.getTrainer().getUser().getIsActive())
                                 .date(training.getDate())
                                 .duration(training.getDuration())
                                 .actionType(actionType)
                                 .build();
    }
}
