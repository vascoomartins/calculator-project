package service;

import dto.CalculationResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PendingRequests {

    private final Map<String, CompletableFuture<CalculationResponse>> pending = new ConcurrentHashMap<>();

    public CompletableFuture<CalculationResponse> register(String requestId) {
        CompletableFuture<CalculationResponse> future = new CompletableFuture<>();
        pending.put(requestId, future);
        return future;
    }

    public void complete(CalculationResponse response) {
        CompletableFuture<CalculationResponse> future = pending.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        }
    }

    public void remove(String requestId) {
        pending.remove(requestId);
    }
}