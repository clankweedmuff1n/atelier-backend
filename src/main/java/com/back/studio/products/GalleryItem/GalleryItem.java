package com.back.studio.products.GalleryItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_gallery")
public class GalleryItem {
    @Id
    @GeneratedValue
    private Long id;
    @JsonProperty("image")
    private String imageUrl;
    private String name;
    private String alt;
    private int width;
    private int height;
    /*@ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;*/
}
