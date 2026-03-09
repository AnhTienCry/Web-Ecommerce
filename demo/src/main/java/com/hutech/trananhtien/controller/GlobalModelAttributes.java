package com.hutech.trananhtien.controller;

import com.hutech.trananhtien.service.CartService;
import com.hutech.trananhtien.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {
    private final CategoryService categoryService;
    private final CartService cartService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("navCategories", categoryService.getAllCategories());
        model.addAttribute("cartItemCount", cartService.getItemCount());
    }
}