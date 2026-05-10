package kr.hhplus.be.server.domain.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.enums.ProductStockReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product_stock_reservation")
public class ProductStockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false)
    private String orderNo;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStockReservationStatus stockReservationStatus;

    @Builder
    private ProductStockReservation(Long id,
                                    String orderNo,
                                    Long productId,
                                    int quantity,
                                    ProductStockReservationStatus stockReservationStatus) {
        this.id = id;
        this.orderNo = orderNo;
        this.productId = productId;
        this.quantity = quantity;
        this.stockReservationStatus = stockReservationStatus;
    }

    public static ProductStockReservation create(String orderNo, Long productId, int quantity) {
        return ProductStockReservation.builder()
                .orderNo(orderNo)
                .productId(productId)
                .quantity(quantity)
                .stockReservationStatus(ProductStockReservationStatus.RESERVED)
                .build();
    }

    public void cancel() {
        this.stockReservationStatus = ProductStockReservationStatus.CANCELLED;
    }

    public void confirm() {
        this.stockReservationStatus = ProductStockReservationStatus.CONFIRMED;
    }
}
