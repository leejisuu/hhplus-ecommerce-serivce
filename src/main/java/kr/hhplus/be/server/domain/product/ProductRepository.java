package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Page<Product> getSellingProducts(ProductStatus productStatus, Pageable pageable);

    Product getSellingProduct(Long productId);
}
