package com.epam.listener;

import com.epam.aspect.TransactionLoggingAspect;
import com.epam.model.dto.TrainerWorkloadDto;
import com.epam.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JMSListener {

    private final TrainerService trainerService;

    @JmsListener(destination = "trainer-workload-queue")
    public void handleMessage(TrainerWorkloadDto trainerDto, @Headers Map<String, Object> headers) {

//        This part is needed for demonstration of dead letter queue
//        if (trainerDto.getDuration() > 10) {
//            throw new RuntimeException("Test");
//        }

        String transactionId = (String) headers.get(TransactionLoggingAspect.TRANSACTION_ID_KEY);
        MDC.put(TransactionLoggingAspect.TRANSACTION_ID_KEY, transactionId);

        trainerService.save(trainerDto);
    }
}
