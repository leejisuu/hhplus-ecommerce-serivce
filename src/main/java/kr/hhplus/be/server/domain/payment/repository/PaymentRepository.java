package kr.hhplus.be.server.domain.payment.repository;

import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.entity.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);

}
