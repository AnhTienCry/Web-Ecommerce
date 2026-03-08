package com.hutech.trananhtien.controller; // Đồng bộ với package thực tế của bạn

import com.hutech.trananhtien.model.Category;
import com.hutech.trananhtien.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/categories") // Gom nhóm các đường dẫn liên quan đến category
@RequiredArgsConstructor // Tự động tạo Constructor cho các field 'final'
public class CategoryController {

    // Sử dụng 'private final' kết hợp @RequiredArgsConstructor là chuẩn nhất hiện nay
    // Bạn không cần thêm @Autowired ở đây nữa vì Lombok đã làm thay
    private final CategoryService categoryService;

    // 1. Hiển thị danh sách danh mục
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories/categories-list"; // Bỏ dấu gạch chéo đầu tiên để Thymeleaf tìm đúng thư mục
    }

    // 2. Hiển thị form thêm danh mục
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/add-category";
    }

    // 3. Xử lý lưu danh mục mới
    @PostMapping("/add")
    public String addCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "categories/add-category"; // Nếu có lỗi validation (như để trống tên) thì quay lại form
        }
        categoryService.addCategory(category);
        return "redirect:/categories"; // Lưu xong thì quay về trang danh sách
    }
}