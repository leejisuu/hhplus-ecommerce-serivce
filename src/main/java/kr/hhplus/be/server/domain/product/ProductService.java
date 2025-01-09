package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.application.order.dto.OrderDetailParam;
import kr.hhplus.be.server.domain.product.dto.OrderDetailCommand;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    public Page<ProductResponse> getSellingProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.getSellingProducts(ProductStatus.SELLING, pageable);
        return productsPage.map(ProductResponse::of);
    }

    public void deductProductStocks(List<OrderDetailCommand> orderDetailCommands) {
        for(OrderDetailCommand command : orderDetailCommands) {
            ProductStock productStock = productStockRepository.getProductStockWithLock(command.productId());
            if(productStock == null) {
                throw new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND);
            }

            productStock.deductStock(command.quantity());
        }
    }

    public Long getTotalNetAmt(List<OrderDetailCommand> orderDetailCommands) {
        Long totalNetAmt = 0L;

        for(OrderDetailCommand command : orderDetailCommands) {
            Product product = productRepository.getProduct(command.productId());
            if(product == null) {
                throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            totalNetAmt += ((long) product.getPrice() * command.quantity());
        }

        return totalNetAmt;
    }
}
