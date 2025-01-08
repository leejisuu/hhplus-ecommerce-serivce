package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponse> getSellingProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.getSellingProducts(ProductStatus.SELLING, pageable);
        return productsPage.map(ProductResponse::of);
    }
}
