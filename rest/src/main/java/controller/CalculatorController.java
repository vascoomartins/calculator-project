package controller;

import dto.ApiResult;
import dto.CalculationRequest;
import dto.CalculationResponse;
import enums.OperationType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/")
public class CalculatorController {

    @Value("${kafka.topic.requests}")
    private String requestTopic;

    private final ReplyingKafkaTemplate<String, CalculationRequest, CalculationResponse> calculationRequestReplyKafkaTemplate;

    public CalculatorController(ReplyingKafkaTemplate<String, CalculationRequest, CalculationResponse> replyingKafkaTemplate) {
        this.calculationRequestReplyKafkaTemplate = replyingKafkaTemplate;
    }

    @GetMapping("/sum")
    public ResponseEntity<?> sum(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return doRequest(OperationType.SUM, a, b);
    }

    @GetMapping("/subtract")
    public ResponseEntity<?> subtract(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return doRequest(OperationType.SUBTRACT, a, b);
    }

    @GetMapping("/multiply")
    public ResponseEntity<?> multiply(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return doRequest(OperationType.MULTIPLY, a, b);
    }

    @GetMapping("/divide")
    public ResponseEntity<?> divide(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return doRequest(OperationType.DIVIDE, a, b);
    }

    private ResponseEntity<?> doRequest(OperationType operation, BigDecimal a, BigDecimal b) {
        String requestId = UUID.randomUUID().toString();

        CalculationRequest request = new CalculationRequest(requestId, operation, a, b);
        ProducerRecord<String, CalculationRequest> record = new ProducerRecord<>(requestTopic, requestId, request);

        try {
            RequestReplyFuture<String, CalculationRequest, CalculationResponse> future =
                    calculationRequestReplyKafkaTemplate.sendAndReceive(record);

            ConsumerRecord<String, CalculationResponse> responseRecord = future.get(3, TimeUnit.SECONDS);
            CalculationResponse response = responseRecord.value();

            if (response.getError() != null) {
                return ResponseEntity.badRequest().body(response.getError());
            }

            return ResponseEntity.ok(new ApiResult(response.getResult()));

        } catch (java.util.concurrent.TimeoutException e) {
            return ResponseEntity.status(504).body("Timeout waiting for calculator response");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}