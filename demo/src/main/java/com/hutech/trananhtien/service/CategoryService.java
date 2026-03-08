package com.hutech.trananhtien.service; // Phải khớp với thư mục service

import com.hutech.trananhtien.model.Category; // Import từ model của bạn
import com.hutech.trananhtien.repository.CategoryRepository; // Import từ repository của bạn
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing categories.
 */
@Service
@RequiredArgsConstructor
@Transactional // Đảm bảo tính toàn vẹn dữ liệu khi thao tác với SQL Server
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Lấy tất cả danh mục từ Database.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Tìm danh mục theo ID.
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Thêm một danh mục mới.
     */
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    /**
     * Cập nhật danh mục hiện có.
     */
    public void updateCategory(@NotNull Category category) {
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalStateException("Category with ID " +
                        category.getId() + " does not exist."));

        existingCategory.setName(category.getName());
        // Trong môi trường @Transactional, không nhất thiết phải gọi save()
        // nhưng gọi save() sẽ giúp code rõ ràng hơn cho đồ án.
        categoryRepository.save(existingCategory);
    }

    /**
     * Xóa danh mục theo ID.
     */
    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalStateException("Category with ID " + id + " does not exist.");
        }
        categoryRepository.deleteById(id);
    }
}