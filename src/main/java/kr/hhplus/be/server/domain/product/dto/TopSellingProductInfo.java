package kr.hhplus.be.server.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TopSellingProductInfo {
    private Long productId;
    private String name;
    private BigDecimal price;
    private int  totalQuantity;

    public static TopSellingProductInfo of(TopSellingProductDto topSellingProductDto) {
        return new TopSellingProductInfo(
                topSellingProductDto.getProductId(),
                topSellingProductDto.getName(),
                topSellingProductDto.getPrice(),
                topSellingProductDto.getTotalQuantity()
        );
    }
}
