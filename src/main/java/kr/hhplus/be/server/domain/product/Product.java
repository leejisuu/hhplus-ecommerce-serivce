package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private int price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_stock_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductStock productStock;

    @Builder
    public Product(String name, ProductStatus status, int price, ProductStock productStock) {
        this.name = name;
        this.status = status;
        this.price = price;
        this.productStock = productStock;
    }
}
