package kafka;

import dto.CalculationRequest;
import dto.CalculationResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import service.CalculatorService;

import java.math.BigDecimal;

@Component
public class CalculationConsumer {
    private final CalculatorService calculatorService;
    private final CalculationResultProducer calculationResultProducer;

    public CalculationConsumer(CalculatorService calculatorService,
                              CalculationResultProducer calculationResultProducer) {
        this.calculatorService = calculatorService;
        this.calculationResultProducer = calculationResultProducer;
    }

    @KafkaListener(topics = "calculation-requests", groupId = "calculator-group")
    public void consume(CalculationRequest request) {
        try {
            BigDecimal result = switch (request.getOperation()) {
                case SUM -> calculatorService.sum(request.getA(), request.getB());
                case SUBTRACT -> calculatorService.subtract(request.getA(), request.getB());
                case MULTIPLY -> calculatorService.multiply(request.getA(), request.getB());
                case DIVIDE -> calculatorService.divide(request.getA(), request.getB());
            };

            calculationResultProducer.send(new CalculationResponse(request.getRequestId(), result, null));
        } catch (Exception e) {
            calculationResultProducer.send(new CalculationResponse(request.getRequestId(), null, e.getMessage()));
        }
    }
}