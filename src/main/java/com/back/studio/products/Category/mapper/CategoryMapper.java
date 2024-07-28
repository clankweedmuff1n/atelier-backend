package com.back.studio.products.Category.mapper;

import com.back.studio.products.Category.Category;
import com.back.studio.products.Category.CategoryRequest;
import com.back.studio.products.GalleryItem.GalleryItemRepository;
import com.back.studio.products.Product.ProductRepository;
import com.back.studio.products.Review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final GalleryItemRepository galleryItemRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.getName())
                .link(categoryRequest.getLink())
                .buttonText(categoryRequest.getButtonText())
                .description(categoryRequest.getDescription())
                .gallery(categoryRequest.getGallery() == null ? null : galleryItemRepository.findAllById(categoryRequest.getGallery()))
                .preview(categoryRequest.getPreviewImage() != null ? galleryItemRepository.findById(categoryRequest.getPreviewImage())
                        .orElse(null) : null)
                .products(categoryRequest.getProducts() == null ? null : productRepository.findAllById(categoryRequest.getProducts()))
                .reviews(categoryRequest.getReviews() == null ? null :
                        reviewRepository.findAllById(categoryRequest.getReviews()))
                .build();
    }
}
