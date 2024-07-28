package com.back.studio.products.GalleryItem.controller;

import com.back.studio.products.GalleryItem.GalleryItem;
import com.back.studio.products.GalleryItem.GalleryItemRequest;
import com.back.studio.products.GalleryItem.service.GalleryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/gallery")
@RequiredArgsConstructor
public class GalleryItemController {
    private final GalleryItemService galleryItemService;

    @PostMapping("/create")
    private GalleryItem createGalleryItem(@RequestBody GalleryItemRequest galleryItemRequest) throws IOException {
        return galleryItemService.createGalleryItem(galleryItemRequest);
    }

    @PostMapping("/create/file")
    public GalleryItem createGalleryItemFromFile(@RequestPart("file") MultipartFile file,
                                                 @RequestParam("width") String width,
                                                 @RequestParam("height") String height) throws IOException {
        return galleryItemService.createGalleryItem(file, width, height);
    }

    /*@PostMapping("/create/all")
    private List<GalleryItem> createGalleryItemAll(@RequestBody List<GalleryItemRequest> galleryItemRequest) {
        return galleryItemService.createGalleryItemAll(galleryItemRequest);
    }

    @PostMapping("/create/file/all")
    public List<GalleryItem> createGalleryItemsFromFiles(@RequestPart("files") List<MultipartFile> files,
                                                         @RequestPart(name = "galleryItemRequests", required = false) List<GalleryItemRequest> galleryItemRequests) throws IOException {
        List<GalleryItem> galleryItems = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            GalleryItemRequest galleryItemRequest = (galleryItemRequests != null && galleryItemRequests.size() > i) ? galleryItemRequests.get(i) : new GalleryItemRequest();

            BufferedImage image = ImageIO.read(file.getInputStream());
            storageService.store(file);
            GalleryItem galleryItem = galleryItemService.createGalleryItem(GalleryItemRequest.builder()
                    .imageUrl(file.getOriginalFilename())
                    .categoryId(galleryItemRequest.getCategoryId())
                    .productId(galleryItemRequest.getProductId())
                    .build(), image.getWidth(), image.getHeight());

            galleryItems.add(galleryItem);
        }
        return galleryItems;
    }*/
}
