package com.back.studio.products.Category.service;

import com.back.studio.products.Category.Category;
import com.back.studio.products.Category.CategoryRepository;
import com.back.studio.products.Category.CategoryRequest;
import com.back.studio.products.Category.mapper.CategoryMapper;
import com.back.studio.products.GalleryItem.GalleryItem;
import com.back.studio.products.GalleryItem.GalleryItemRepository;
import com.back.studio.products.Product.Product;
import com.back.studio.products.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final GalleryItemRepository galleryItemRepository;
    private final CategoryMapper mapper;

    //@PostConstruct
    private void init() {
        Category category = Category.builder()
                .description("best category")
                .name("tarts")
                .build();

        category = categoryRepository.save(category);


        List<GalleryItem> savedGallery = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GalleryItem galleryItem = GalleryItem.builder()
                    //.category(category)
                    .imageUrl("THIS IS IMAGE URL " + i)
                    .build();
            savedGallery.add(galleryItemRepository.save(galleryItem));
        }

        category.setGallery(savedGallery.subList(0, 3));

        category = categoryRepository.save(category);

        Product product = Product.builder()
                .name("chocolate tart")
                .price(100L)
                .category(category)
                .productType("tart")
                .preview(savedGallery.get(2))
                .gallery(savedGallery)
                .description("the best chocolate tart")
                .build();

        product = productRepository.save(product);

        System.out.println(categoryRepository.findAll());
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getByLink(String link) {
        return categoryRepository.findByLink(link)
                .orElse(null);
    }

    public Category getByProduct(Product product) {
        return null;
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElse(null);
    }

    public Category createCategory(CategoryRequest categoryRequest) {
        return categoryRepository.save(mapper.toCategory(categoryRequest));
    }

    public List<Category> createCategoryAll(List<CategoryRequest> categoryRequests) {
        return categoryRepository.saveAll(categoryRequests.stream().map(mapper::toCategory).toList());
    }
}
