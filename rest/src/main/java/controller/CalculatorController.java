package controller;

import dto.ApiResult;
import dto.CalculationRequest;
import dto.CalculationResponse;
import enums.OperationType;
import kafka.CalculationRequestProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.PendingRequests;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/")
public class CalculatorController {

    private final CalculationRequestProducer calculationRequestProducer;
    private final PendingRequests pendingRequests;

    public CalculatorController(CalculationRequestProducer calculationRequestProducer,
                                PendingRequests pendingRequests) {
        this.calculationRequestProducer = calculationRequestProducer;
        this.pendingRequests = pendingRequests;
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
        CompletableFuture<CalculationResponse> future = pendingRequests.register(requestId);

        calculationRequestProducer.send(request);

        try {
            CalculationResponse response = future.get(3, TimeUnit.SECONDS);

            if (response.getError() != null) {
                return ResponseEntity.badRequest().body(response.getError());
            }

            return ResponseEntity.ok(new ApiResult(response.getResult()));
        } catch (Exception e) {
            pendingRequests.remove(requestId);
            return ResponseEntity.status(504).body("Timeout waiting for calculator response");
        }
    }
}