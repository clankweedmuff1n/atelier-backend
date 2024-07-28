package com.back.studio.products.Review.service;

import com.back.studio.products.Review.Review;
import com.back.studio.products.Review.ReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewRequest reviewRequest);
    List<Review> createReviewAll(List<ReviewRequest> reviewRequests);
}
