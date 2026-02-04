import controller.CalculatorController;
import dto.ApiResult;
import dto.CalculationResponse;
import kafka.CalculationRequestProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.PendingRequests;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @Mock
    private CalculationRequestProducer calculationRequestProducer;

    @Mock
    private PendingRequests pendingRequests;

    @InjectMocks
    private CalculatorController calculatorController;

    @Test
    void testSum() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");

        CompletableFuture<CalculationResponse> future =
                CompletableFuture.completedFuture(new CalculationResponse("any", new BigDecimal("15"), null));
        Mockito.when(pendingRequests.register(Mockito.anyString())).thenReturn(future);

        ResponseEntity<?> response = calculatorController.sum(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResult body = (ApiResult) response.getBody();
        assertEquals(new BigDecimal("15"), body.getResult());
        Mockito.verify(calculationRequestProducer).send(Mockito.any());
    }

    @Test
    void testSubtract() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");

        CompletableFuture<CalculationResponse> future =
                CompletableFuture.completedFuture(new CalculationResponse("any", new BigDecimal("5"), null));
        Mockito.when(pendingRequests.register(Mockito.anyString())).thenReturn(future);

        ResponseEntity<?> response = calculatorController.subtract(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResult body = (ApiResult) response.getBody();
        assertEquals(new BigDecimal("5"), body.getResult());
        Mockito.verify(calculationRequestProducer).send(Mockito.any());
    }

    @Test
    void testMultiply() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");

        CompletableFuture<CalculationResponse> future =
                CompletableFuture.completedFuture(new CalculationResponse("any", new BigDecimal("50"), null));
        Mockito.when(pendingRequests.register(Mockito.anyString())).thenReturn(future);

        ResponseEntity<?> response = calculatorController.multiply(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResult body = (ApiResult) response.getBody();
        assertEquals(new BigDecimal("50"), body.getResult());
        Mockito.verify(calculationRequestProducer).send(Mockito.any());
    }

    @Test
    void testDivide() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("2");

        CompletableFuture<CalculationResponse> future =
                CompletableFuture.completedFuture(new CalculationResponse("any", new BigDecimal("5"), null));
        Mockito.when(pendingRequests.register(Mockito.anyString())).thenReturn(future);

        ResponseEntity<?> response = calculatorController.divide(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResult body = (ApiResult) response.getBody();
        assertEquals(new BigDecimal("5"), body.getResult());
        Mockito.verify(calculationRequestProducer).send(Mockito.any());
    }

    @Test
    void testDivideByZero() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = BigDecimal.ZERO;

        CompletableFuture<CalculationResponse> future =
                CompletableFuture.completedFuture(new CalculationResponse("any", null, "Division by zero is not allowed"));
        Mockito.when(pendingRequests.register(Mockito.anyString())).thenReturn(future);

        ResponseEntity<?> response = calculatorController.divide(a, b);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Division by zero is not allowed", response.getBody());
        Mockito.verify(calculationRequestProducer).send(Mockito.any());
    }
}