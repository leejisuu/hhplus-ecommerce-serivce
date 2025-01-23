package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductStockService {

    private final ProductStockRepository productStockRepository;

    @Transactional
    public void deductQuantity(Long productId, Integer quantity) {
        ProductStock productStock = productStockRepository.getProductStockWithLock(productId);
        if(productStock == null) {
            throw new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND);
        }

        productStock.deductQuantity(quantity);
    }
}
