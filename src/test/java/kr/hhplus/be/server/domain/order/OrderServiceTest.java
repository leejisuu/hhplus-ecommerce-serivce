package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    void 주문_조회_시_주문_정보가_없으면_CustomException_예외를_발생한다() {
        // given
        Long orderId = 1L;

        BDDMockito.given(orderRepository.getOrder(orderId))
                .willReturn(null);

        // when // then
        assertThatThrownBy(() -> orderService.getOrder(orderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());

        verify(orderRepository, times(1)).getOrder(orderId);

    }

}