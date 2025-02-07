package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.product.dto.TopSellingProductInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TopSellingProductResponse {
    private Long productId;
    private String name;
    private BigDecimal price;
    private int  totalQuantity;

    public static TopSellingProductResponse of(TopSellingProductInfo topSellingProductInfo) {
        return new TopSellingProductResponse(
                topSellingProductInfo.getProductId(),
                topSellingProductInfo.getName(),
                topSellingProductInfo.getPrice(),
                topSellingProductInfo.getTotalQuantity()
        );
    }
}
