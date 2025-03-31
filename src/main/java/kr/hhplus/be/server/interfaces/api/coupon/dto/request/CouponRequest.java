package kr.hhplus.be.server.interfaces.api.coupon.dto.request;

import kr.hhplus.be.server.domain.coupon.dto.command.CouponCommand;

public class CouponRequest {

    public record AddQueue (
        Long userId,
        Long couponId
    ) {
        public CouponCommand.AddQueue toAddQueueCommand(long currentMillis) {
            return new CouponCommand.AddQueue(userId, couponId, currentMillis);
        }
    }
}
