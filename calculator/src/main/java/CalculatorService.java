import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculatorService {

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    public BigDecimal sum(BigDecimal a, BigDecimal b) {
        return a.add(b, MATH_CONTEXT);
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b, MATH_CONTEXT);
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b, MATH_CONTEXT);
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }

        return a.divide(b, MATH_CONTEXT);
    }
}
