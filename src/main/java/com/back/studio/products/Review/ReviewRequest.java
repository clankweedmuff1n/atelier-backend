package com.back.studio.products.Review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String author;
    private String description;
    @JsonProperty("avatar")
    private String avatarUrl;
    private RateType rate;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("product_id")
    private Long productId;
}
