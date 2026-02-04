import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {
    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    void testSum() {
        BigDecimal result = calculatorService.sum(new BigDecimal("2.5"), new BigDecimal("3.5"));
        assertEquals(new BigDecimal("6"), result.stripTrailingZeros());
    }

    @Test
    void testSubtract() {
        BigDecimal result = calculatorService.subtract(new BigDecimal("5.5"), new BigDecimal("2.0"));
        assertEquals(new BigDecimal("3.5"), result.stripTrailingZeros());
    }

    @Test
    void testMultiply() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("2"), new BigDecimal("3.5"));
        assertEquals(new BigDecimal("7"), result.stripTrailingZeros());
    }

    @Test
    void testDivide() {
        BigDecimal result = calculatorService.divide(new BigDecimal("7"), new BigDecimal("2"));
        assertEquals(new BigDecimal("3.5"), result.stripTrailingZeros());
    }

    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () ->
                calculatorService.divide(new BigDecimal("5"), BigDecimal.ZERO)
        );
    }
}