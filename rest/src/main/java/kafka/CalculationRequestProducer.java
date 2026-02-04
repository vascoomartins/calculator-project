package kafka;

import dto.CalculationRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CalculationRequestProducer {
    private static final String TOPIC = "calculation-requests";

    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    public CalculationRequestProducer(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CalculationRequest request) {
        kafkaTemplate.send(TOPIC, request.getRequestId(), request);
    }
}