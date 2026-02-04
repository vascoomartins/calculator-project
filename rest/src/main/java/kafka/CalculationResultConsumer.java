package kafka;

import dto.CalculationResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import service.PendingRequests;

@Component
public class CalculationResultConsumer {

    private final PendingRequests pendingRequests;

    public CalculationResultConsumer(PendingRequests pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @KafkaListener(topics = "calculation-results", groupId = "rest-group")
    public void consume(CalculationResponse response) {
        pendingRequests.complete(response);
    }
}