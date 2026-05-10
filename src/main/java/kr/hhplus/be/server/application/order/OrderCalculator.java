package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.client.ProductClient;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderCalculator {

    public Map<Long, BigDecimal> buildPriceMap(List<ProductClient.Product> products) {
        return products.stream()
                .collect(Collectors.toMap(ProductClient.Product::id, ProductClient.Product::price));
    }

    public BigDecimal calculateTotalAmt(OrderCriteria.Create criteria, Map<Long, BigDecimal> priceMap) {
        return criteria.details().stream()
                .map(d -> priceMap.get(d.productId()).multiply(new BigDecimal(d.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
