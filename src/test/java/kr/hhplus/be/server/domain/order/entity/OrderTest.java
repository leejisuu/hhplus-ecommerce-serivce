package kr.hhplus.be.server.domain.order.entity;

import kr.hhplus.be.server.domain.order.enums.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class OrderTest {

    @Test
    void 주문_상태를_PAID로_변경한다() {
        // given
        Long userId = 1L;
        BigDecimal totalOriginalAmt = new BigDecimal("15000");
        Order order = Order.create(userId, totalOriginalAmt);

        // when
        order.completePayment();

        // then
        Assertions.assertThat(order.getStatus().name()).isEqualTo(OrderStatus.PAID.name());
    }
}
