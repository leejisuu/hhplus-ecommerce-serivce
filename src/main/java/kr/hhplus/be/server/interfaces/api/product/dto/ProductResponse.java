package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private String status;
    private int stock;
    private int price;

    @Builder
    public ProductResponse(Long id, String name, String status, int stock, int price) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.stock = stock;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .status(product.getStatus().name())
                .stock(product.getProductStock().getQuantity())
                .price(product.getPrice())
                .build();
    }
}
