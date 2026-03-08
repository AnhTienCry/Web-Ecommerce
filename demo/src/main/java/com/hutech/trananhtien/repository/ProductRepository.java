package com.hutech.trananhtien.repository;
import com.hutech.trananhtien.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Code này chạy tốt cho cả MySQL lẫn SQL Server
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Bạn có thể thêm các hàm tìm kiếm tùy chỉnh tại đây nếu cần
    // Ví dụ: List<Product> findByNameContaining(String name);
}