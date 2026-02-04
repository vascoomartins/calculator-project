package kafka;

import dto.CalculationResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CalculationResultProducer {
    private static final String TOPIC = "calculation-results";

    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    public CalculationResultProducer(KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CalculationResponse response) {
        kafkaTemplate.send(TOPIC, response.getRequestId(), response);
    }
}