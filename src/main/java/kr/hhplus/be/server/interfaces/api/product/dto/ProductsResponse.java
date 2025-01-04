package kr.hhplus.be.server.interfaces.api.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductsResponse {
    private List<ProductResponse> products;

    @Builder
    public ProductsResponse(List<ProductResponse> products) {
        this.products = products;
    }
}
