package com.back.studio.products.GalleryItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryItemRequest {
    private String name;
    private String alt;
    @JsonProperty("image")
    private String imageUrl;
    private Integer width;
    private Integer height;
}
