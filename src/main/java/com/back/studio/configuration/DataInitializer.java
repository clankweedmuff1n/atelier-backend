package com.back.studio.configuration;

import com.back.studio.auth.user.Role;
import com.back.studio.auth.user.User;
import com.back.studio.auth.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @PostConstruct
    CommandLineRunner init() {
        log.info("Initializing data...");
        userRepository.findByEmail("admin@admin.com").ifPresentOrElse(
                user -> log.info("User with email [{}] already exists", user.getEmail()),
                () -> {
                    User user = new User();
                    user.setEmail("admin@admin.com");
                    user.setConfirmed(true);
                    user.setPassword(passwordEncoder.encode("admin"));
                    user.setRole(Role.ADMIN);
                    log.info("User with email [{}] created", userRepository.save(user).getEmail());
                }
        );
        return args -> {
            //createCategoriesWithProducts();
        };
    }

       /* private void createCategoriesWithProducts() {
            List<CategoryRequest> categories = List.of(
                    new CategoryRequest("Платья", "Платья для женщин на любой случай", "dresses", "Перейти в каталог", "https://i.ibb.co/nDzG7WT/IMG-6260.jpg"),
                    new CategoryRequest("Спортивные костюмы", "Костюмы в стиле Sport-style отличаются по крою и материалам", "sport", "Перейти в каталог", "https://i.ibb.co/DVRtb9c/IMG-6382.jpg"),
                    new CategoryRequest("Пиджаки", "Женские пиджаки", "blazers", "Перейти в каталог", "https://i.ibb.co/cQKpTBH/IMG-6258.jpg"),
                    new CategoryRequest("Блузы", "Женские блузы", "blouse", "Перейти в каталог", "https://i.ibb.co/3Cbgdqq/DSCF4286.webp"),
                    new CategoryRequest("Брюки", "Женские брюки", "trousers", "Перейти в каталог", "https://i.ibb.co/w4CWLKS/DSCF4973.webp"),
                    new CategoryRequest("Костюмы", "Костюмы", "costumes", "Перейти в каталог", "https://i.ibb.co/kcD9vpz/DSCF5386.jpg")
            );

            categories.forEach(this::createCategoryWithProducts);
        }

        private void createCategoryWithProducts(CategoryRequest categoryRequest) {
            GalleryItem previewItem = createGalleryItem(categoryRequest.getPreview());
            categoryRequest.setPreviewId(previewItem.getId());

            Category category = categoryService.createCategory(categoryRequest);

            // Example product creation logic for "trousers" category
            if ("trousers".equals(category.getLink())) {
                createProductForCategory(category, "Брюки", "brown-trousers", List.of("https://i.ibb.co/w4CWLKS/DSCF4973.webp"));
            }

            // Add other categories and products as needed
        }

        private GalleryItem createGalleryItem(String imageUrl) {
            GalleryItemRequest galleryItemRequest = new GalleryItemRequest(imageUrl, 100, 100); // Assuming fixed dimensions for simplicity
            return galleryItemService.createGalleryItem(galleryItemRequest);
        }

        private void createProductForCategory(Category category, String name, String link, List<String> imageUrls) {
            List<GalleryItem> galleryItems = imageUrls.stream()
                    .map(this::createGalleryItem)
                    .collect(Collectors.toList());

            ProductRequest productRequest = new ProductRequest(
                    name, "", "random_type", link, List.of("first detail", "second detail"),
                    List.of("first comp", "second comp"), Math.round(Math.random() * 100),
                    category.getId(), galleryItems.stream().map(GalleryItem::getId).collect(Collectors.toList())
            );

            productService.createProduct(productRequest);
        }*/
}
