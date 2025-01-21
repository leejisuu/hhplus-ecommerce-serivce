package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.order.repository.OrderDetailRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Nested
    @DisplayName("주문 생성 단위 테스트")
    class CreateOrder {
        @Test
        void 주문_상세_목록이_비어있으면_예외를_발생한다() {
            // given
            Long userId = 1L;
            List<OrderCommand.OrderDetail> orderDetailsCommand = new ArrayList<>();

            OrderCommand.Order orderCommand = new OrderCommand.Order(userId, orderDetailsCommand);

            // when // then
            assertThatThrownBy(() -> orderService.order(orderCommand))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ORDER_DETAILS_NOT_EXISTS.getMessage());
        }

        @Test
        void 주문을_생성한다() {
            Long userId = 1L;
            Long orderId = 1L;

            List<OrderCommand.OrderDetail> orderDetailsCommand = List.of(
                    new OrderCommand.OrderDetail(1L, 10, new BigDecimal(2500)),
                    new OrderCommand.OrderDetail(3L, 5, new BigDecimal(3200))
            );

            OrderCommand.Order orderCommand = new OrderCommand.Order(userId, orderDetailsCommand);

            BigDecimal totalOriginalAmt = orderCommand.details().stream()
                    .map(detail -> new BigDecimal(detail.quantity()).multiply(detail.price()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Order mockOrder = Order.create(userId, totalOriginalAmt);

            List<OrderDetail> mockOrderDetails = orderCommand.details().stream()
                    .map(orderDetail -> OrderDetail.create(
                            mockOrder,
                            orderDetail.productId(),
                            orderDetail.quantity(),
                            orderDetail.price()
                    ))
                    .toList();

            given(orderRepository.save(any(Order.class)))
                    .willReturn(mockOrder);

            given(orderDetailRepository.saveAll(anyList())).willReturn(mockOrderDetails);

            // when
            OrderInfo.OrderDto order = orderService.order(orderCommand);

            // then
            assertThat(order.totalOriginalAmt().compareTo(totalOriginalAmt)).isEqualTo(0);

            verify(orderRepository, times(1)).save(any(Order.class));
            verify(orderDetailRepository, times(1)).saveAll(anyList());
        }
    }

    @Nested
    @DisplayName("주문 조회 단위 테스트")
    class GetOrder {
        @Test
        void 주문_정보_조회_시_주문_정보가_없다면_예외를_발생한다() {
            // given
            Long orderId = 1L;

            given(orderRepository.findById(orderId)).willReturn(null);

            // when // then
            Assertions.assertThatThrownBy(() -> orderService.getOrder(orderId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());

            verify(orderRepository, times(1)).findById(orderId);
        }

        @Test
        void 주문_정보를_조회한다() {
            // given
            Long userId = 1L;
            Long orderId = 1L;
            BigDecimal totalOriginalAmt = new BigDecimal("15000");
            Order mockOrder = Order.create(userId, totalOriginalAmt);

            given(orderRepository.findById(orderId)).willReturn(mockOrder);

            // when
            OrderInfo.OrderDto order = orderService.getOrder(orderId);

            // then
            assertThat(order.totalOriginalAmt().compareTo(totalOriginalAmt)).isEqualTo(0);

            verify(orderRepository, times(1)).findById(orderId);

        }
    }
}
