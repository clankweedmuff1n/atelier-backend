package com.back.studio.products.GalleryItem.service;

import com.back.studio.products.GalleryItem.GalleryItem;
import com.back.studio.products.GalleryItem.GalleryItemRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface GalleryItemService {
    GalleryItem createGalleryItem(GalleryItemRequest galleryItemRequest) throws IOException;

    GalleryItem createGalleryItem(MultipartFile file, String width, String height) throws IOException;

    List<GalleryItem> createGalleryItemAll(List<GalleryItemRequest> galleryItemRequests);
}
