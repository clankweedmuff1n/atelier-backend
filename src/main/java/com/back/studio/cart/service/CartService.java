package com.back.studio.cart.service;

import com.back.studio.auth.user.User;
import com.back.studio.products.Product.Product;

import java.util.List;

public interface CartService {
    void addToCart(Product product, User user);
    void addToCartAll(List<Product> products, User user);
    void removeFromCart(Long productId, User user);
}
