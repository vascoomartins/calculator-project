package dto;

import enums.OperationType;

import java.math.BigDecimal;

public class CalculationRequest {
    private String requestId;
    private OperationType operation;
    private BigDecimal a;
    private BigDecimal b;

    public CalculationRequest() {}

    public CalculationRequest(String requestId, OperationType operation, BigDecimal a, BigDecimal b) {
        this.requestId = requestId;
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    public String getRequestId() {
        return requestId;
    }

    public BigDecimal getA() {
        return a;
    }

    public BigDecimal getB() {
        return b;
    }
}
