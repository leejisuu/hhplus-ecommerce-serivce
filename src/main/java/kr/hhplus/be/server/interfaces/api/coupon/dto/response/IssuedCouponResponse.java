package kr.hhplus.be.server.interfaces.api.coupon.dto.response;

import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IssuedCouponResponse {

    public record Coupon(
            Long id,
            String name,
            String discountType,
            BigDecimal discountAmt,
            LocalDateTime issuedAt,
            LocalDateTime validStartedAt,
            LocalDateTime validEndedAt,
            LocalDateTime usedAt
    ) {
        public static IssuedCouponResponse.Coupon of(IssuedCouponInfo.Coupon issuedCouponInfo) {
            return new IssuedCouponResponse.Coupon(
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

        public static Page<IssuedCouponResponse.Coupon> mapToResponsePage(Page<IssuedCouponInfo.Coupon> coupons) {
            return coupons.map(IssuedCouponResponse.Coupon::of);
        }
    }
}

