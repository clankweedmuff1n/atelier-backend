package com.back.studio.products.Product;

import com.back.studio.auth.user.User;
import com.back.studio.products.Category.Category;
import com.back.studio.products.GalleryItem.GalleryItem;
import com.back.studio.products.Review.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_products")
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @JsonProperty("product_type")
    private String productType;
    private Long price;
    @Column(unique = true)
    private String link;
    @ElementCollection
    private List<String> details;
    @ElementCollection
    private List<String> composition;
    @CreationTimestamp
    @JsonProperty("created_at")
    private Date createdAt;
    @UpdateTimestamp
    @JsonProperty("updated_at")
    private Date updatedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    @ToString.Exclude
    private Category category;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Review> reviews;
    @OneToOne(fetch = FetchType.EAGER)
    private GalleryItem preview;
    @OneToMany(fetch = FetchType.EAGER)
    private List<GalleryItem> gallery;

    @ManyToMany(mappedBy = "cart")
    @JsonIgnore
    private List<User> usersInCart;

    @ManyToMany(mappedBy = "wishlist")
    @JsonIgnore
    private List<User> usersInWishlist;
}
