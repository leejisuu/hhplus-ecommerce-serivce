package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.order.dto.command.OrderDetailCommand;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    public Page<ProductResponse> getSellingProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.getSellingProducts(ProductStatus.SELLING, pageable);
        return productsPage.map(ProductResponse::from);
    }

    public Product getSellingProduct(Long productId) {
        return productRepository.getSellingProduct(productId);
    }
}
