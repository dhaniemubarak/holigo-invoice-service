package id.holigo.services.holigoinvoiceservice.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.PaymentDto;
import id.holigo.services.common.model.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto implements Serializable {

    private UUID id;
    private String invoiceNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private BigDecimal discountAmount;
    private BigDecimal fareAmount;
    private String indexProduct;
    private String transactionId;
    private String transactionType;
    private UUID paymentId;
    private PaymentDto payment;
    private PaymentStatusEnum paymentStatus;
    private OrderStatusEnum orderStatus;
    private JsonNode detail;
}
