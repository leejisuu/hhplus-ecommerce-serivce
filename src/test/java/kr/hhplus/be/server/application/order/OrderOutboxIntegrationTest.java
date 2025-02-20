package kr.hhplus.be.server.application.order;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutbox;
import kr.hhplus.be.server.infrastructure.order.outbox.OrderCreatedOutboxJpaRepository;
import kr.hhplus.be.server.interfaces.scheuler.OrderCreatedOutboxScheduler;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderOutboxIntegrationTest extends IntegrationTestSupport {

    @Autowired
    OrderApplicationService orderApplicationService;

    @Autowired
    OrderCreatedOutboxJpaRepository orderCreatedOutboxJpaRepository;

    @Autowired
    OrderCreatedOutboxScheduler orderCreatedOutboxScheduler;

    @Test
    void 주문을_완료하면_주문_완료_아웃박스_테이블에_한_로우가_추가된다() {
        // given
        Long userId = 1L;
        List<OrderCriteria.OrderDetail> orderDetailsCriteria = List.of(
                new OrderCriteria.OrderDetail(1L, 9999),
                new OrderCriteria.OrderDetail(7L, 1)
        );
        OrderCriteria.Order orderCriteria = new OrderCriteria.Order(userId, orderDetailsCriteria);

        // 주문 전 order_created_outbox 테이블 조회
        List<OrderCreatedOutbox> beforeOutboxes = orderCreatedOutboxJpaRepository.findAll();

        // when
        orderApplicationService.order(orderCriteria);

        // then
        // 주문 후 order_created_outbox 테이블 조회
        List<OrderCreatedOutbox> afterOutboxes = orderCreatedOutboxJpaRepository.findAll();

        assertThat(beforeOutboxes).hasSize(0);
        assertThat(afterOutboxes).hasSize(1);
    }

    @Test
    void 주문을_완료하면_아웃박스_상태가_비동기적으로_INIT에서_COMPLETE로_변경된다() {
        // given
        Long userId = 1L;
        List<OrderCriteria.OrderDetail> orderDetailsCriteria = List.of(
                new OrderCriteria.OrderDetail(1L, 9999),
                new OrderCriteria.OrderDetail(7L, 1)
        );
        OrderCriteria.Order orderCriteria = new OrderCriteria.Order(userId, orderDetailsCriteria);

        // when
        orderApplicationService.order(orderCriteria);

        // then
        // 주문 직후 아웃 박스 조회
        OrderCreatedOutbox beforeOutbox = orderCreatedOutboxJpaRepository.findAll().get(0);

        // 10초 대기
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 아웃 박스 조회
        OrderCreatedOutbox afterOutbox = orderCreatedOutboxJpaRepository.findAll().get(0);

        assertThat(beforeOutbox.getStatus()).isEqualTo(OutboxStatus.INIT);
        assertThat(afterOutbox.getStatus()).isEqualTo(OutboxStatus.COMPLETE);
    }

    @Test
    void 주문_완료_아웃박스의_상태가_INIT이라면_다시_메세지를_발행하여_COMPLETE으로_변경한다() {
        // given
        Long orderId = 1L;
        OrderEvent.Created event = OrderEvent.Created.create(orderId, BigDecimal.valueOf(10000));
        String eventToJson = "";
        try {
            eventToJson = new ObjectMapper().writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        OrderCreatedOutbox orderCreatedOutbox = OrderCreatedOutbox.create(event.messageId(), orderId, eventToJson);
        OrderCreatedOutbox beforeOutbox = orderCreatedOutboxJpaRepository.save(orderCreatedOutbox);

        // when
        orderCreatedOutboxScheduler.resendOrderCreatedMessage();

        // 10초 대기
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // then
        OrderCreatedOutbox afterOutbox = orderCreatedOutboxJpaRepository.findAll().get(0);

        assertThat(beforeOutbox.getStatus()).isEqualTo(OutboxStatus.INIT);
        assertThat(afterOutbox.getStatus()).isEqualTo(OutboxStatus.COMPLETE);
    }
}
