package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.client.ProductClient;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {

    public void validateProducts(OrderCriteria.Create criteria, List<ProductClient.Product> products) {
        if (criteria.details().size() != products.size()) {
            throw new CustomException(ErrorCode.ORDER_PRODUCT_COUNT_MISMATCH);
        }
        criteria.details().forEach(detail ->
            products.stream()
                    .filter(p -> p.id().equals(detail.productId()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }
}
