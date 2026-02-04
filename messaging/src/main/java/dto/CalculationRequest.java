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

    public OperationType getOperation() { return operation; }

    public BigDecimal getA() {
        return a;
    }

    public BigDecimal getB() {
        return b;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public void setA(BigDecimal a) {
        this.a = a;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

}
