package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentMakeRequest;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentMakeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PaymentFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserService userService;

    @Transactional
    public PaymentMakeResponse makePayment(PaymentMakeRequest request) {
        Long orderId = request.orderId();
        Long userId = request.userId();

        User user = userService.getUserWithLock(userId);

        Order order = orderService.getOrder(orderId);
        user.deductPoint(order.getTotalAmt());

        return paymentService.makePayment(order);
    }
}
