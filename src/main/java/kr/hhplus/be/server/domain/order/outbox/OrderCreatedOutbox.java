package kr.hhplus.be.server.domain.order.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseCreatedAtEntity;
import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.order.event.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_created_outbox")
public class OrderCreatedOutbox extends BaseCreatedAtEntity {
    @Id
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Lob
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String payload;

    public static OrderCreatedOutbox create(String messageId, Long orderId, String payload) {
        return new OrderCreatedOutbox(messageId, orderId, OutboxStatus.INIT, payload);
    }

    public static OrderCreatedOutbox of(OrderEvent.Created event) {
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("OrderEvent.Created json parsing error", e);
        }

        return OrderCreatedOutbox.create(event.messageId(), event.orderId(), payload);
    }

    public void complete() {
        this.status = OutboxStatus.COMPLETE;
    }
}

