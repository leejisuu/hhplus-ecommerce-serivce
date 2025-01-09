package kr.hhplus.be.server.domain.product.dto;

import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.user.entity.User;

import java.util.List;

public record OrderCommand(
        User user,
        int netAmt,
        int discountAmt,
        IssuedCoupon issuedCoupon,
        List<OrderDetail> orderDetails
) {
    public static OrderCommand create(User user, int netAmt, int discountAmt, IssuedCoupon issuedCoupon, List<OrderDetail> orderDetails) {
        return new OrderCommand(
                user,
                netAmt,
                discountAmt,
                issuedCoupon,
                orderDetails
        );
    }
}
