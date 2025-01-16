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

    @Column(nullable = false)
    private int quantity;

    @Builder
    private ProductStock(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void deductQuantity(int quantity) {
        if(this.quantity - quantity < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }

        this.quantity -= quantity;
    }
}
