package dto;

import java.math.BigDecimal;

public class CalculationResponse {

    private String requestId;
    private BigDecimal result;
    private String error;

    public CalculationResponse() {}

    public CalculationResponse(String requestId, BigDecimal result, String error) {
        this.requestId = requestId;
        this.result = result;
        this.error = error;
    }

    public String getRequestId() {
        return requestId;
    }

    public BigDecimal getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public void setError(String error) {
        this.error = error;
    }
}
