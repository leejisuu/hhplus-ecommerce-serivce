package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.entity.Product;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    List<Product> getTopSellingProducts(LocalDate todayDate, int limit);
}
