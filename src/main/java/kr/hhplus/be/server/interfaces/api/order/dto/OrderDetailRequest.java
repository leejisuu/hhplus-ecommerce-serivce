package kr.hhplus.be.server.interfaces.api.order.dto;

import lombok.Builder;
import lombok.Getter;

public record OrderDetailRequest (
        Long productId,
        int quantity
) {
}

