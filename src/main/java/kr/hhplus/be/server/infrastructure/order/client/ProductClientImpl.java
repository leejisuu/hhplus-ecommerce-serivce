package kr.hhplus.be.server.infrastructure.order.client;

import kr.hhplus.be.server.application.order.client.ProductClient;
import kr.hhplus.be.server.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    private final ProductService productService;

    @Override
    public List<Product> getProducts(List<Long> productIds) {
        return productService.getProducts(productIds).stream()
                .map(p -> new Product(p.id(), p.name(), p.price()))
                .toList();
    }
}
