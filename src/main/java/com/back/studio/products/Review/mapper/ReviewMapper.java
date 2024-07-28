package com.back.studio.products.Review.mapper;

import com.back.studio.products.Category.CategoryRepository;
import com.back.studio.products.Product.ProductRepository;
import com.back.studio.products.Review.Review;
import com.back.studio.products.Review.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public Review toReview(ReviewRequest reviewRequest) {
        return Review.builder()
                .author(reviewRequest.getAuthor())
                .rate(reviewRequest.getRate())
                .avatarUrl(reviewRequest.getAvatarUrl())
                .category(reviewRequest.getCategoryId() != null ? categoryRepository.findById(reviewRequest.getCategoryId())
                        .orElse(null) : null)
                .description(reviewRequest.getDescription())
                .product(reviewRequest.getProductId() != null ? productRepository.findById(reviewRequest.getProductId())
                        .orElse(null) : null)
                .build();
    }
}
