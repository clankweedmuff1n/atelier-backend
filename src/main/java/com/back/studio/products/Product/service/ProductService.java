package com.back.studio.products.Product.service;

import com.back.studio.products.Product.Product;
import com.back.studio.products.Product.ProductRequest;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductRequest productRequest);
    List<Product> createProductAll(List<ProductRequest> productRequests);
    List<Product> getAllProducts();
    List<Product> getAllProductsById(List<Long> productIds);
    Product getById(Long id);
    void deleteProduct(String id);
}
