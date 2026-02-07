
import controller.CalculatorController;
import dto.ApiResult;
import dto.CalculationRequest;
import dto.CalculationResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @Mock
    private ReplyingKafkaTemplate<String, CalculationRequest, CalculationResponse> replyingKafkaTemplate;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(calculatorController, "requestTopic", "calculation-requests");
    }

    @Test
    void testSum() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        CalculationResponse response = new CalculationResponse("test-id", new BigDecimal("15"), null);

        RequestReplyFuture<String, CalculationRequest, CalculationResponse> mockFuture = mock(RequestReplyFuture.class);
        ConsumerRecord<String, CalculationResponse> consumerRecord = new ConsumerRecord<>("topic", 0, 0L, "key", response);

        when(replyingKafkaTemplate.sendAndReceive(any(ProducerRecord.class))).thenReturn(mockFuture);
        when(mockFuture.get(anyLong(), any())).thenReturn(consumerRecord);

        ResponseEntity<?> result = calculatorController.sum(a, b);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResult body = (ApiResult) result.getBody();
        assertEquals(new BigDecimal("15"), body.getResult());
    }

    @Test
    void testSubtract() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        CalculationResponse response = new CalculationResponse("test-id", new BigDecimal("5"), null);

        RequestReplyFuture<String, CalculationRequest, CalculationResponse> mockFuture = mock(RequestReplyFuture.class);
        ConsumerRecord<String, CalculationResponse> consumerRecord = new ConsumerRecord<>("topic", 0, 0L, "key", response);

        when(replyingKafkaTemplate.sendAndReceive(any(ProducerRecord.class))).thenReturn(mockFuture);
        when(mockFuture.get(anyLong(), any())).thenReturn(consumerRecord);

        ResponseEntity<?> result = calculatorController.subtract(a, b);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResult body = (ApiResult) result.getBody();
        assertEquals(new BigDecimal("5"), body.getResult());
    }

    @Test
    void testMultiply() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        CalculationResponse response = new CalculationResponse("test-id", new BigDecimal("50"), null);

        RequestReplyFuture<String, CalculationRequest, CalculationResponse> mockFuture = mock(RequestReplyFuture.class);
        ConsumerRecord<String, CalculationResponse> consumerRecord = new ConsumerRecord<>("topic", 0, 0L, "key", response);

        when(replyingKafkaTemplate.sendAndReceive(any(ProducerRecord.class))).thenReturn(mockFuture);
        when(mockFuture.get(anyLong(), any())).thenReturn(consumerRecord);

        ResponseEntity<?> result = calculatorController.multiply(a, b);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResult body = (ApiResult) result.getBody();
        assertEquals(new BigDecimal("50"), body.getResult());
    }

    @Test
    void testDivide() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("2");
        CalculationResponse response = new CalculationResponse("test-id", new BigDecimal("5"), null);

        RequestReplyFuture<String, CalculationRequest, CalculationResponse> mockFuture = mock(RequestReplyFuture.class);
        ConsumerRecord<String, CalculationResponse> consumerRecord = new ConsumerRecord<>("topic", 0, 0L, "key", response);

        when(replyingKafkaTemplate.sendAndReceive(any(ProducerRecord.class))).thenReturn(mockFuture);
        when(mockFuture.get(anyLong(), any())).thenReturn(consumerRecord);

        ResponseEntity<?> result = calculatorController.divide(a, b);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResult body = (ApiResult) result.getBody();
        assertEquals(new BigDecimal("5"), body.getResult());
    }

    @Test
    void testDivideByZero() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = BigDecimal.ZERO;
        CalculationResponse response = new CalculationResponse("test-id", null, "Division by zero is not allowed");

        RequestReplyFuture<String, CalculationRequest, CalculationResponse> mockFuture = mock(RequestReplyFuture.class);
        ConsumerRecord<String, CalculationResponse> consumerRecord = new ConsumerRecord<>("topic", 0, 0L, "key", response);

        when(replyingKafkaTemplate.sendAndReceive(any(ProducerRecord.class))).thenReturn(mockFuture);
        when(mockFuture.get(anyLong(), any())).thenReturn(consumerRecord);

        ResponseEntity<?> result = calculatorController.divide(a, b);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Division by zero is not allowed", result.getBody());
    }
}