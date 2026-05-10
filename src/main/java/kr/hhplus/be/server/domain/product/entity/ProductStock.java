package kr.hhplus.be.server.domain.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_stock")
public class ProductStock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Builder
    private ProductStock(Long productId, int totalQuantity, int reservedQuantity) {
        this.productId = productId;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = reservedQuantity;
    }

    public static ProductStock create(Long productId, int totalQuantity, int reservedQuantity) {
        return new ProductStock(productId, totalQuantity, reservedQuantity);
    }

    public void reserve(int quantity) {
        if(totalQuantity - (reservedQuantity + quantity) <= 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }

        reservedQuantity += quantity;
    }

    public void cancel(int quantity) {
        this.reservedQuantity -= quantity;
    }

    public int getAvailableQuantity() {
        return this.totalQuantity - this.reservedQuantity;
    }
}
