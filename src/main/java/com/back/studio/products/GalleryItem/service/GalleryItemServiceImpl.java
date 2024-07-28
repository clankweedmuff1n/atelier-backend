package com.back.studio.products.GalleryItem.service;

import com.back.studio.files.StorageService;
import com.back.studio.products.GalleryItem.GalleryItem;
import com.back.studio.products.GalleryItem.GalleryItemRepository;
import com.back.studio.products.GalleryItem.GalleryItemRequest;
import com.back.studio.products.GalleryItem.mapper.GalleryItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryItemServiceImpl implements GalleryItemService {
    private final GalleryItemRepository galleryItemRepository;
    private final GalleryItemMapper mapper;
    private final StorageService storageService;

    public GalleryItem createGalleryItem(GalleryItemRequest galleryItemRequest) {
        return galleryItemRepository.save(mapper.toGalleryItem(galleryItemRequest));
    }

    public GalleryItem createGalleryItem(MultipartFile file, String width, String height) {
        String oldFileName = file.getOriginalFilename();
        String newFileName = storageService.store(file);
        System.out.println(newFileName);
        return galleryItemRepository.save(mapper.toGalleryItem(newFileName, oldFileName, oldFileName, Integer.parseInt(width), Integer.parseInt(height)));
    }

    public List<GalleryItem> createGalleryItemAll(List<GalleryItemRequest> galleryItemRequests) {
        return galleryItemRepository.saveAll(galleryItemRequests.stream().map(mapper::toGalleryItem).toList());
    }
}
