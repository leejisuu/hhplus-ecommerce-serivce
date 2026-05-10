package kr.hhplus.be.server.application.order.client;

import java.math.BigDecimal;
import java.util.List;

public interface ProductClient {
    List<Product> getProducts(List<Long> productIds);

    record Product(Long id, String name, BigDecimal price) {}
}
