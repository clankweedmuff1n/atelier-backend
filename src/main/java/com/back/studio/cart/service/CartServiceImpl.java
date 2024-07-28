package com.back.studio.cart.service;

import com.back.studio.auth.UserService;
import com.back.studio.auth.user.User;
import com.back.studio.products.Product.Product;
import com.back.studio.products.Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserService userService;
    private final ProductService productService;

    @Override
    public synchronized void addToCart(Product product, User userDTO) {
        if (product == null || userDTO == null) {
            throw new IllegalArgumentException("Product and User cannot be null");
        }

        User managedUser = userService.getById(userDTO.getId());
        Set<Product> cart = managedUser.getCart();

        if (cart == null) {
            cart = Collections.synchronizedSet(new HashSet<>());
            managedUser.setCart(cart);
        }

        cart.add(product);
        userService.updateUser(managedUser);
    }

    @Override
    public void addToCartAll(List<Product> products, User user) {
        if (products == null || user == null || products.isEmpty()) {
            throw new IllegalArgumentException("Product and User cannot be null");
        }

        User managedUser = userService.getById(user.getId());
        Set<Product> cart = managedUser.getCart();

        if (cart == null) {
            cart = Collections.synchronizedSet(new HashSet<>());
            managedUser.setCart(cart);
        }

        cart.addAll(products);
        userService.updateUser(managedUser);
    }

    @Override
    public synchronized void removeFromCart(Long productId, User user) {
        if (productId == null || user == null) {
            throw new IllegalArgumentException("ProductId and User cannot be null");
        }

        Product product = productService.getById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with given ID does not exist");
        }

        User managedUser = userService.getById(user.getId());
        Set<Product> cart = managedUser.getCart();

        if (cart != null && cart.remove(product)) {
            userService.updateUser(managedUser);
        }
    }

}
