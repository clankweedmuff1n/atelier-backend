package com.back.studio.products.Category.service;


import com.back.studio.products.Category.Category;
import com.back.studio.products.Category.CategoryRequest;
import com.back.studio.products.Product.Product;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest categoryRequest);
    List<Category> createCategoryAll(List<CategoryRequest> categoryRequests);
    List<Category> getAll();
    Category getByLink(String link);
    Category getByProduct(Product product);
    Category getById(Long id);
}
