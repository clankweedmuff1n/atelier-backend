package com.back.studio.wishlist.service;

import com.back.studio.auth.user.User;
import com.back.studio.products.Product.Product;

import java.util.List;

public interface WishListService {
    void addToWishList(Product product, User user);
    void addToWishListAll(List<Product> products, User user);
    void removeFromWishList(Long productId, User user);
}
