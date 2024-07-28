package com.back.studio;

import com.back.studio.products.Category.Category;
import com.back.studio.products.Category.CategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudioApplicationTests {
    /*private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String baseUrl = "http://localhost:8000/api/v1";

    @AfterEach
    void afterEach() {
        String sqlQuery =
                """
                        delete from _category;
                        delete from _gallery;
                        delete from _products;
                        delete from _reviews;
                        """;
        jdbcTemplate.update(sqlQuery);
    }

    @Test
    @Order(0)
    void shouldCreateCategory() {
        String categoryName = "Empty category";
        String categoryDescription = "Empty category description";

        CategoryRequest request = CategoryRequest.builder()
                .name(categoryName)
                .description(categoryDescription)
                .build();

        Category category = restTemplate.postForObject(baseUrl + "/category/create", request, Category.class);

        assertThat(category)
                .isNotNull()
                .extracting(
                        Category::getName,
                        Category::getDescription,
                        Category::getGallery,
                        Category::getPreview,
                        Category::getProducts,
                        Category::getReviews
                )
                .containsExactly(
                        categoryName,
                        categoryDescription,
                        null,
                        null,
                        null,
                        null
                );
    }

    @Test
    @Order(1)
    void shouldCreateTwoCategories() {
        String categoryName = "Empty category";
        String categoryDescription = "Empty category description";

        List<CategoryRequest> categoryRequestList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            CategoryRequest request = CategoryRequest.builder()
                    .name(categoryName + i)
                    .description(categoryDescription + i)
                    .build();

            categoryRequestList.add(request);
        }

        Category[] category = restTemplate.postForObject(baseUrl + "/category/create/all", categoryRequestList, Category[].class);

        assertThat(category)
                .isNotNull()
                .hasSize(2)
                .extracting(
                        Category::getId,
                        Category::getName,
                        Category::getDescription,
                        Category::getGallery,
                        Category::getPreview,
                        Category::getProducts,
                        Category::getReviews
                )
                .containsExactly(
                        tuple(1L, categoryName + "0", categoryDescription + "0", null, null, null, null),
                        tuple(2L, categoryName + "1", categoryDescription + "1", null, null, null, null)
                );
    }



    @Test
    @Order(2)
    void getAllCategories() {
        Category[] categories = restTemplate.getForObject(baseUrl + "/category/all", Category[].class);
        assertThat(categories)
                .isNotNull()
                .isEmpty();
    }*/
}
