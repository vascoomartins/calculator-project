package dto;

import java.math.BigDecimal;

public class ApiResult {

    private BigDecimal result;

    public ApiResult() {
    }

    public ApiResult(BigDecimal result) {
        this.result = result;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }
}