package com.hutech.trananhtien.controller;

import com.hutech.trananhtien.model.Product;
import com.hutech.trananhtien.service.CategoryService;
import com.hutech.trananhtien.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // 1. Hiển thị danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/product-list";
    }

    // 2. Hiển thị form thêm sản phẩm mới
    @GetMapping("/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/product-form";
    }

    // 3. Lưu sản phẩm (Thêm mới hoặc Cập nhật) với upload ảnh
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/product-form";
        }

        // Xử lý upload ảnh nếu có file được chọn
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = saveImage(imageFile);
                product.setImageUrl(imageUrl);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Lỗi khi upload ảnh: " + e.getMessage());
                model.addAttribute("categories", categoryService.getAllCategories());
                return "product/product-form";
            }
        } else if (product.getId() != null) {
            // Nếu đang update và không có ảnh mới, giữ ảnh cũ
            Product existingProduct = productService.getProductById(product.getId());
            if (existingProduct != null) {
                product.setImageUrl(existingProduct.getImageUrl());
            }
        }

        productService.saveProduct(product);
        return "redirect:/products";
    }

    // 4. Hiển thị form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/product-form";
    }

    // 5. Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // Helper method: Lưu ảnh và trả về đường dẫn
    private String saveImage(MultipartFile file) throws IOException {
        // Tạo thư mục uploads nếu chưa tồn tại
        Path uploadPath = Paths.get("src/main/resources/static/" + uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên file unique
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        // Lưu file
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về đường dẫn relative để hiển thị trong HTML
        return "/" + uploadDir + "/" + newFilename;
    }
}