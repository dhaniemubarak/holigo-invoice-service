package id.holigo.services.holigoinvoiceservice.web.model;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.PaymentDtoForUser;
import id.holigo.services.common.model.PaymentStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDtoForUser {

    private UUID id;

    private String invoiceNumber;

    private Timestamp createdAt;

    private Timestamp expiredAt;

    private BigDecimal discountAmount;

    private BigDecimal fareAmount;

    private BigDecimal ntaAmount;

    private BigDecimal nraAmount;

    private BigDecimal hpAmount;

    private BigDecimal adminAmount;

    private String indexProduct;

    private String transactionId;

    private String transactionType;

    private UUID paymentId;

    private PaymentDtoForUser payment;

    private PaymentStatusEnum paymentStatus;

    private OrderStatusEnum orderStatus;

    private Integer serviceId;

    private Integer productId;

    private Object detail;

    @Builder.Default
    private Timestamp serverTime = Timestamp.valueOf(LocalDateTime.now());
}
