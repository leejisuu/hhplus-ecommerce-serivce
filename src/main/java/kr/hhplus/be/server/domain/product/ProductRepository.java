package kr.hhplus.be.server.domain.product;

import org.springframework.stereotype.Component;

import java.util.List;

public interface ProductRepository {

    List<Product> getSellingProducts(ProductStatus productStatus);
}
