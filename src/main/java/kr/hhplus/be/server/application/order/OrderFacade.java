package kr.hhplus.be.server.application.order;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.order.dto.OrderCreateParam;
import kr.hhplus.be.server.application.order.dto.OrderDetailParam;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.dto.OrderDetailCommand;
import kr.hhplus.be.server.domain.user.UserService;
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

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateParam orderCreateParam, LocalDateTime currentTime) {


        List<OrderDetailCommand> orderDetailCommands = orderCreateParam.products().stream()
                .map(OrderDetailParam::toCommand) // 변환 메서드 호출
                .toList();

        // 비관적 락 걸어 상품 재고 정보 조회 후 재고 차감
        productService.deductProductStocks(orderDetailCommands);

        // 상품 정보 조회해서 슨수 구매 금액 반환
        Long totalNetAmt = productService.getTotalNetAmt(orderDetailCommands);

        // 쿠폰 할인 금액 계산
        IssuedCoupon issuedCoupon;
        if(orderCreateParam.couponId() != null) {
            issuedCoupon = couponService.getIssuedCouponWithLock(orderCreateParam.couponId(), orderCreateParam.userId(), currentTime);

            // 쿠폰 사용(쿠폰 상태를 "USED"로 변경)
            issuedCoupon.useCoupon();

            Long discountAmt = issuedCoupon.calculateDiscountAmt(totalNetAmt);
        }

        // 주문 정보 저장

        return orderService.createOrder(order);

    }
}
