package com.back.studio.products.GalleryItem.mapper;

import com.back.studio.products.GalleryItem.GalleryItem;
import com.back.studio.products.GalleryItem.GalleryItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GalleryItemMapper {
    public GalleryItem toGalleryItem(GalleryItemRequest galleryItemRequest) {
        return GalleryItem.builder()
                .imageUrl(galleryItemRequest.getImageUrl())
                .name(galleryItemRequest.getName())
                .alt(galleryItemRequest.getAlt())
                .width(galleryItemRequest.getWidth())
                .height(galleryItemRequest.getHeight())
                .build();
    }

    public GalleryItem toGalleryItem(String imageUrl, String name, String alt, int width, int height) {
        return GalleryItem.builder()
                .imageUrl(imageUrl)
                .name(name)
                .alt(alt)
                .width(width)
                .height(height)
                .build();
    }
}
