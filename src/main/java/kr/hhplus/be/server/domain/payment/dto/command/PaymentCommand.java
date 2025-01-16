package kr.hhplus.be.server.domain.payment.dto.command;

import lombok.Builder;

public class PaymentCommand {

    public record Payment(

    ) {
        @Builder
        public Payment {}
    }
}
