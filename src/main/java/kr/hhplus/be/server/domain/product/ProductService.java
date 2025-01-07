package kr.hhplus.be.server.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getSellingProducts() {
        return productRepository.getSellingProducts(ProductStatus.SELLING);
    }
}
