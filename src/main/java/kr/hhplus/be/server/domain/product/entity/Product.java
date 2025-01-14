package kr.hhplus.be.server.domain.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.product.ProductSellingStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "selling_status", nullable = false)
    private ProductSellingStatus sellingStatus;

    @Column(nullable = false)
    private BigDecimal price;

    @Builder
    private Product(String name, ProductSellingStatus sellingStatus, BigDecimal price) {
        this.name = name;
        this.sellingStatus = sellingStatus;
        this.price = price;
    }
}
