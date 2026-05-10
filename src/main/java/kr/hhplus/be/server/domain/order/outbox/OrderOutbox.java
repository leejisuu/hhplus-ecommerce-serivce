package kr.hhplus.be.server.domain.order.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseCreatedAtEntity;
import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.order.event.OrderEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_outbox")
public class OrderOutbox extends BaseCreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(nullable = false, name = "order_no")
    private String orderNo;

    @Column(nullable = false, name = "event_type")
    @Enumerated(EnumType.STRING)
    private OrderOutboxEventType eventType;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Builder
    private OrderOutbox(Long id, String eventId, String orderNo, OrderOutboxEventType eventType, OutboxStatus status, String payload) {
        this.id = id;
        this.eventId = eventId;
        this.orderNo = orderNo;
        this.eventType = eventType;
        this.status = status;
        this.payload = payload;
    }

    public static OrderOutbox create(String eventId,
                                     String orderNo,
                                     OrderOutboxEventType eventType,
                                     String payload) {
        return OrderOutbox.builder()
                .eventId(eventId)
                .orderNo(orderNo)
                .eventType(eventType)
                .status(OutboxStatus.INIT)
                .payload(payload)
                .build();
    }

    public static OrderOutbox of(OrderEvent.Confirmed event) {
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("OrderEvent.Confirmed json parsing error", e);
        }

        return OrderOutbox.create(event.eventId(), event.orderNo(), OrderOutboxEventType.CONFIRMED, payload);
    }

    public void complete() {
        this.status = OutboxStatus.COMPLETE;
    }
}
