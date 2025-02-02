package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.StockCommand;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductStockService {

    private final ProductStockRepository productStockRepository;

    public void deductQuantity(StockCommand.OrderDetails command) {
        for (StockCommand.OrderDetail orderDetail : command.details()) {
            ProductStock productStock = productStockRepository.getProductStock(orderDetail.productId());
            if(productStock == null) {
                throw new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND);
            }

            productStock.deductQuantity(orderDetail.quantity());

            productStockRepository.save(productStock);
        }
    }
}
