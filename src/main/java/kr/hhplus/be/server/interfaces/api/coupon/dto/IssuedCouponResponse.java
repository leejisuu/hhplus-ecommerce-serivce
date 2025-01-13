package kr.hhplus.be.server.interfaces.api.coupon.dto;

import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 레코드는 자동으로 모든 필드에 대한 생성자, 접근자 메서드, equals(), hashCode(), toString() 메서드를 생성
public record IssuedCouponResponse (
        Long id,
        String name,
        String discountType,
        BigDecimal discountAmt,
        LocalDateTime issuedAt,
        LocalDateTime validStartedAt,
        LocalDateTime validEndedAt,
        LocalDateTime usedAt
) {
    public static IssuedCouponResponse from(IssuedCouponInfo issuedCouponInfo) {
        return new IssuedCouponResponse(
                issuedCouponInfo.id(),
                issuedCouponInfo.name(),
                issuedCouponInfo.discountType(),
                issuedCouponInfo.discountAmt(),
                issuedCouponInfo.issuedAt(),
                issuedCouponInfo.validStartedAt(),
                issuedCouponInfo.validEndedAt(),
                issuedCouponInfo.usedAt()
        );
    }

    public static Page<IssuedCouponResponse> mapToResponsePage(Page<IssuedCouponInfo> coupons) {
        return coupons.map(IssuedCouponResponse::from);
    }
}
