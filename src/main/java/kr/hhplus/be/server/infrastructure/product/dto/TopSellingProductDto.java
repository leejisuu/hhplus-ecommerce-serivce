package kr.hhplus.be.server.infrastructure.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TopSellingProductDto {
    private Long productId;
    private String name;
    private BigDecimal price;
    private int  totalQuantity;

    public TopSellingProductDto(Long productId, String name, BigDecimal price, int totalQuantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.totalQuantity = totalQuantity;
    }
}
