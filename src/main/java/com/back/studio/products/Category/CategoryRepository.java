package com.back.studio.products.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>  {
    Optional<Category> findByLink(String link);
}
