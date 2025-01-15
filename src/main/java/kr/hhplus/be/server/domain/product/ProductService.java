package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.entity.repository.ProductRepository;
import kr.hhplus.be.server.infrastructure.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductInfo> getSellingProducts(Pageable pageable) {
        Page<ProductDTO> productDTOPage = productRepository.getSellingProducts(pageable);
        return productDTOPage.map(ProductInfo::of);
    }
}
