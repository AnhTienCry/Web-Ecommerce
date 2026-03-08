package com.hutech.trananhtien.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor // BẮT BUỘC có để JPA hoạt động
@AllArgsConstructor
@Entity
// 'orders' là từ khóa trong SQL, nhưng Hibernate sẽ tự động xử lý tốt cho bạn
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tương thích hoàn toàn với IDENTITY của SQL Server
    private Long id;

    private String customerName;

    // Quan hệ 1-N với OrderDetail
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}