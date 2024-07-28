package com.back.studio.products.Product.service;

import com.back.studio.products.Product.Product;
import com.back.studio.products.Product.ProductRepository;
import com.back.studio.products.Product.ProductRequest;
import com.back.studio.products.Product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public Product createProduct(ProductRequest productRequest) {
        return productRepository.save(mapper.toProduct(productRequest));
    }

    public List<Product> createProductAll(List<ProductRequest> productRequests) {
        return productRepository.saveAll(productRequests.stream().map(mapper::toProduct).toList());
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductsById(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }


    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElse(null);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(Long.valueOf(id));
    }
}
