package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.order.dto.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.dto.info.OrderCreateInfo;
import kr.hhplus.be.server.domain.order.dto.info.TopSellingProductInfo;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStockRepository;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public List<TopSellingProductInfo> getTopSellingProducts(LocalDate todayDate, int limit) {
        return orderRepository.getTopSellingProducts(todayDate, limit);
    }

    @Transactional
    public OrderCreateInfo createOrder(OrderCreateCommand command, LocalDateTime currentTime) {
        // 유저 조회
        User user = userRepository.findById(command.userId());

        List<OrderDetail> orderDetails = command.orderDetails().stream()
                .map(cmd -> {
                    // 판매중인 상품만 조회. 판매중이 아니라면 조회가 안되므로 예외 발생
                    Product product = productRepository.getSellingProduct(cmd.productId());
                    if (product == null) {
                        throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
                    }

                    // 재고 비관적 락으로 조회해서 재고 차감
                    ProductStock productStock = productStockRepository.getProductStockWithLock(cmd.productId());
                    productStock.deductStock(cmd.quantity());

                    return OrderDetail.create(product, cmd.quantity(), product.getPrice());
                })
                .collect(Collectors.toList());

        // 사용할 쿠폰 조회
        IssuedCoupon coupon = null;
        if(command.couponId() != null) {
            coupon = issuedCouponRepository.getIssuedCouponWithLock(command.userId(), command.couponId());
        }

        Order order = Order.create(user, coupon, orderDetails, currentTime);

        return OrderCreateInfo.from(orderRepository.createOrder(order));
    }
}
