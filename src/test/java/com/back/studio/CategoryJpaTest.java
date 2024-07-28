package com.back.studio;

import com.back.studio.products.Category.Category;
import com.back.studio.products.Category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryJpaTest {
    /*private final CategoryRepository repository;

    @Test
    void getAllCategories() {
        Category category = Category.builder()
                .name("test")
                .description("test")
                .build();
        repository.save(category);
        System.out.println("SIZE " + repository.findAll().size());
    }*/
}
