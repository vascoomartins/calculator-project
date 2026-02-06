package kafka;

import dto.CalculationRequest;
import dto.CalculationResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import service.CalculatorService;

import java.math.BigDecimal;

@Component
public class CalculationConsumer {

    @Value("${kafka.topic.requests}")
    private String requestTopic;

    @Value("${kafka.topic.results}")
    private String resultTopic;

    private final CalculatorService calculatorService;
    private final KafkaTemplate<String, CalculationResponse> calculationResponseKafkaTemplate;

    public CalculationConsumer(CalculatorService calculatorService,
                               KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.calculatorService = calculatorService;
        this.calculationResponseKafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${kafka.topic.requests}", groupId = "calculator-group",
            containerFactory = "calculationRequestListenerContainerFactory")
    public void consume(@Payload CalculationRequest request,
                        @Header(value = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopicBytes,
                        @Header(value = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationIdBytes) {

        CalculationResponse response;
        try {
            BigDecimal result = switch (request.getOperation()) {
                case SUM -> calculatorService.sum(request.getA(), request.getB());
                case SUBTRACT -> calculatorService.subtract(request.getA(), request.getB());
                case MULTIPLY -> calculatorService.multiply(request.getA(), request.getB());
                case DIVIDE -> calculatorService.divide(request.getA(), request.getB());
            };

            response = new CalculationResponse(request.getRequestId(), result, null);

        } catch (Exception e) {
            response = new CalculationResponse(request.getRequestId(), null, e.getMessage());
        }

        ProducerRecord<String, CalculationResponse> replyRecord =
                new ProducerRecord<>(resultTopic, request.getRequestId(), response);

        if (correlationIdBytes != null) {
            replyRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationIdBytes);
        }

        calculationResponseKafkaTemplate.send(replyRecord);
    }
}