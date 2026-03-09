package com.hutech.trananhtien.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sản phẩm là bắt buộc")
    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;

    @PositiveOrZero(message = "Giá phải lớn hơn hoặc bằng 0")
    private double price;

    @Column(name = "old_price")
    private Double oldPrice;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String badge;

    @Column(name = "status_text", columnDefinition = "NVARCHAR(255)")
    private String statusText;

    private boolean promo;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public String getPriceDisplay() {
        return java.text.NumberFormat.getInstance().format(price);
    }

    public String getOldPriceDisplay() {
        if (oldPrice == null) {
            return null;
        }
        return java.text.NumberFormat.getInstance().format(oldPrice);
    }
}
