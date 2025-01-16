package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private IssuedCouponRepository issuedCouponRepository;

    @Test
    void 발급된_쿠폰이_없다면_예외를_발생한다() {
        // given
        Long issuedCouponId = 1L;
        LocalDateTime currentTime = LocalDateTime.now();

        given(issuedCouponRepository.getIssuedCouponWithLock(issuedCouponId, currentTime)).willReturn(null);

        // when // then
        Assertions.assertThatThrownBy(() -> couponService.getIssuedCouponWithLock(issuedCouponId, currentTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ISSUED_COUPON_NOT_FOUND.getMessage());
    }
}