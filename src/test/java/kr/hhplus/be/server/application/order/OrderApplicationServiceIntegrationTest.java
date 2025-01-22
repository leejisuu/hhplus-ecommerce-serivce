package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderApplicationServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Test
    void 주문_싱품의_상품_마스터_정보가_판매종료_이거나_존재하지_않는다면_예외를_발생한다() {
        // given
        Long userId = 1L;
        List<OrderCriteria.OrderDetail> orderDetailsCriteria = List.of(
                new OrderCriteria.OrderDetail(1L, 10),
                new OrderCriteria.OrderDetail(2L, 3),
                new OrderCriteria.OrderDetail(6L, 2)
        );

        OrderCriteria.Order orderCriteria = new OrderCriteria.Order(userId, orderDetailsCriteria);

        // when // then
        assertThatThrownBy(() -> orderApplicationService.order(orderCriteria))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void 주문_개수가_상품_보유_재고보다_크면_예외를_발생한다() {
        // given
        Long userId = 1L;
        List<OrderCriteria.OrderDetail> orderDetailsCriteria = List.of(
                new OrderCriteria.OrderDetail(1L, 9999),
                new OrderCriteria.OrderDetail(7L, 301)
        );

        OrderCriteria.Order orderCriteria = new OrderCriteria.Order(userId, orderDetailsCriteria);

        // when // then
        assertThatThrownBy(() -> orderApplicationService.order(orderCriteria))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_STOCK.getMessage());
    }

    @Test
    void 주문을_생성한다() {
        // given
        Long userId = 1L;

        int quantity1 = 9999;
        int quantity2 = 1;

        BigDecimal price1 = new BigDecimal(2500);
        BigDecimal price2 = new BigDecimal(4500);

        List<OrderCriteria.OrderDetail> orderDetailsCriteria = List.of(
                new OrderCriteria.OrderDetail(1L, quantity1),
                new OrderCriteria.OrderDetail(7L, quantity2)
        );

        OrderCriteria.Order orderCriteria = new OrderCriteria.Order(userId, orderDetailsCriteria);

        BigDecimal totalOriginalAmt = price1.multiply(new BigDecimal(quantity1)).add(price2.multiply(new BigDecimal(quantity2)));

        // when
        OrderResult.Order order = orderApplicationService.order(orderCriteria);

        // then
        assertThat(order).isNotNull();
        assertThat(order.totalOriginalAmt().compareTo(totalOriginalAmt)).isEqualTo(0);
    }
}
