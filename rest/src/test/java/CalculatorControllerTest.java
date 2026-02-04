import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {
    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;

    @Test
    void testSum() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("15");

        Mockito.when(calculatorService.sum(a, b)).thenReturn(expected);

        ResponseEntity<BigDecimal> response = calculatorController.sum(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testSubtract() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("5");

        Mockito.when(calculatorService.subtract(a, b)).thenReturn(expected);

        ResponseEntity<BigDecimal> response = calculatorController.subtract(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testMultiply() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("50");

        Mockito.when(calculatorService.multiply(a, b)).thenReturn(expected);

        ResponseEntity<BigDecimal> response = calculatorController.multiply(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testDivide() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("2");
        BigDecimal expected = new BigDecimal("5");

        Mockito.when(calculatorService.divide(a, b)).thenReturn(expected);

        ResponseEntity<BigDecimal> response = calculatorController.divide(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testDivideByZero() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = BigDecimal.ZERO;

        Mockito.when(calculatorService.divide(a, b))
                .thenThrow(new ArithmeticException("Division by zero"));

        ResponseEntity<BigDecimal> response = calculatorController.divide(a, b);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}