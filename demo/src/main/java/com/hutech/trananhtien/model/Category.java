package com.hutech.trananhtien.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List; // Thêm dòng này vào
@Setter
@Getter
@NoArgsConstructor // Cần thiết cho JPA hoạt động
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên là bắt buộc")
    private String name;

    // Nếu muốn liên kết ngược lại với Product (Không bắt buộc nhưng nên có)
     @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
     private List<Product> products;
}