package kr.hhplus.be.server.application.order;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.order.dto.OrderCreateParam;
import kr.hhplus.be.server.application.order.dto.OrderDetailParam;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.dto.OrderCommand;
import kr.hhplus.be.server.domain.product.dto.OrderDetailCommand;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OrderFacade {

    private final CouponService couponService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateParam orderCreateParam, LocalDateTime currentTime) {
        User user = userService.getUser(orderCreateParam.userId());

        List<OrderDetailCommand> orderDetailCommands = orderCreateParam.products().stream()
                .map(OrderDetailParam::toCommand)
                .toList();

        // 비관적 락 걸어 상품 재고 정보 조회 후 재고 차감
        productService.deductProductStocks(orderDetailCommands);

        // 상품 정보 조회 및 매핑
        List<OrderDetail> orderDetails = orderDetailCommands.stream()
                .map(command -> {
                    Product product = productService.getSellingProduct(command.productId());
                    return OrderDetail.from(command, product);
                })
                .collect(Collectors.toList());

        // 상품 정보 조회해서 순수 구매 금액 반환
        int netAmt = productService.getNetAmt(orderDetailCommands);

        // 쿠폰 사용 처리 및 쿠폰 할인 금액 계산
        int discountAmt = 0;
        IssuedCoupon issuedCoupon = null;
        if (orderCreateParam.couponId() != null) {
            issuedCoupon = couponService.getIssuedCouponWithLock(orderCreateParam.couponId(), orderCreateParam.userId(), currentTime);
            issuedCoupon.useCoupon();
            discountAmt = issuedCoupon.calculateDiscountAmt(netAmt);
        }

        return orderService.createOrder(Order.create(user, netAmt, discountAmt, issuedCoupon, orderDetails));

    }
}
