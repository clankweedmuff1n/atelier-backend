package com.back.studio.products.Category.controller;

import com.back.studio.products.Category.Category;
import com.back.studio.products.Category.CategoryRequest;
import com.back.studio.products.Category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/link/{link}")
    private Category getCategoryByLink(@PathVariable String link) {
        return categoryService.getByLink(link);
    }

    @GetMapping("/all")
    private List<Category> getAllCategories() {
        return categoryService.getAll();
    }

    @PostMapping("/create")
    private Category createCategory(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.createCategory(categoryRequest);
    }

    @PostMapping("/create/all")
    private List<Category> createCategory(@RequestBody List<CategoryRequest> categoryRequests) {
        return categoryService.createCategoryAll(categoryRequests);
    }
}
