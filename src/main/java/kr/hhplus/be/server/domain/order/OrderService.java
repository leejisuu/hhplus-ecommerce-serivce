package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public List<ProductResponse> getTopSellingProducts(LocalDate todayDate, int limit) {
        List<Product> products = orderRepository.getTopSellingProducts(todayDate, limit);
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
