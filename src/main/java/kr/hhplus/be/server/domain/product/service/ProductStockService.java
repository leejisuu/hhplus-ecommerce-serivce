package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductStockService {

    private final ProductStockRepository productStockRepository;

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public void deductQuantity(Long productId, Integer quantity) {
        log.info("deductQuantity() 호출됨 - productId: {}, quantity: {}", productId, quantity);

        ProductStock productStock = productStockRepository.getProductStock(productId);
        if(productStock == null) {
            throw new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND);
        }

        productStock.deductQuantity(quantity);
    }
}
