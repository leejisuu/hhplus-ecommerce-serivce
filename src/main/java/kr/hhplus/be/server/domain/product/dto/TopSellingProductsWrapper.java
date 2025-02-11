package kr.hhplus.be.server.domain.product.dto;

import java.io.Serializable;
import java.util.List;

public class TopSellingProductsWrapper implements Serializable {
    private List<ProductInfo.TopSelling> topSellingProducts;

    public TopSellingProductsWrapper() {}

    public TopSellingProductsWrapper(List<ProductInfo.TopSelling> topSellingProducts) {
        this.topSellingProducts = topSellingProducts;
    }

    public List<ProductInfo.TopSelling> getTopSellingProducts() {
        return topSellingProducts;
    }
}
