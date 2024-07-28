package com.back.studio.products.Category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String name;
    private String description;
    @JsonProperty("preview")
    private Long previewImage;
    private String link;
    @JsonProperty("button_text")
    private String buttonText;
    private List<Long> gallery;
    private List<Long> products;
    private List<Long> reviews;
    /*private GalleryItemRequest previewImage;
    private List<GalleryItemRequest> gallery;
    private List<ProductRequest> products;
    private List<ReviewRequest> reviews;*/
}
