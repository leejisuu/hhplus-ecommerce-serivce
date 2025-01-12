package kr.hhplus.be.server.domain.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.product.ProductStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private int price;

    @OneToOne(mappedBy = "product")
    private ProductStock productStock;

    @Builder
    public Product(String name, ProductStatus status, int price, ProductStock productStock) {
        this.name = name;
        this.status = status;
        this.price = price;
        this.productStock = productStock;
    }
}
