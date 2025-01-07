package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @Builder
    public ProductStock(int quantity) {
        this.quantity = quantity;
    }

    public void deductStock(int quantity) {
        if(this.quantity - quantity < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }

        this.quantity -= quantity;
    }
}
