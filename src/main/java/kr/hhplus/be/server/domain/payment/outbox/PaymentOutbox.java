package kr.hhplus.be.server.domain.payment.outbox;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseCreatedAtEntity;
import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.payment.event.PaymentEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payment_outbox")
public class PaymentOutbox extends BaseCreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(nullable = false, name = "order_no")
    private String orderNo;

    @Column(nullable = false, name = "event_type")
    @Enumerated(EnumType.STRING)
    private PaymentOutboxEventType eventType;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Builder
    private PaymentOutbox(Long id, String eventId, String orderNo, PaymentOutboxEventType eventType, OutboxStatus status, String payload) {
        this.id = id;
        this.eventId = eventId;
        this.orderNo = orderNo;
        this.eventType = eventType;
        this.status = status;
        this.payload = payload;
    }

    public static PaymentOutbox create(String eventId,
                                       String orderNo,
                                       PaymentOutboxEventType eventType,
                                       String payload) {
        return PaymentOutbox.builder()
                .eventId(eventId)
                .orderNo(orderNo)
                .eventType(eventType)
                .status(OutboxStatus.INIT)
                .payload(payload)
                .build();
    }

    public static PaymentOutbox of(PaymentEvent.Paid event) {
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("PaymentEvent.Paid json parsing error", e);
        }

        return PaymentOutbox.create(event.eventId(), event.orderNo(), PaymentOutboxEventType.COMPLETED, payload);
    }

    public static PaymentOutbox of(PaymentEvent.Failed event) {
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("PaymentEvent.Failed json parsing error", e);
        }

        return PaymentOutbox.create(event.eventId(), event.orderNo(), PaymentOutboxEventType.FAILED, payload);
    }

    public void complete() {
        this.status = OutboxStatus.COMPLETE;
    }
}