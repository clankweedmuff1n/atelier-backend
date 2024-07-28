package com.back.studio.products.Review.service;

import com.back.studio.products.Review.Review;
import com.back.studio.products.Review.ReviewRepository;
import com.back.studio.products.Review.ReviewRequest;
import com.back.studio.products.Review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper mapper;

    public Review createReview(ReviewRequest reviewRequest) {
        return reviewRepository.save(mapper.toReview(reviewRequest));
    }

    public List<Review> createReviewAll(List<ReviewRequest> reviewRequests) {
        return reviewRepository.saveAll(reviewRequests.stream().map(mapper::toReview).toList());
    }
}
