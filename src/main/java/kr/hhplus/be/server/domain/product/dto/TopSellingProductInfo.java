package kr.hhplus.be.server.domain.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class TopSellingProductInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 6740615042958756423L;
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
