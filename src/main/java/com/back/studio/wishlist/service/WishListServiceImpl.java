package com.back.studio.wishlist.service;

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
public class WishListServiceImpl implements WishListService {
    private final UserService userService;
    private final ProductService productService;
    
    @Override
    public void addToWishList(Product product, User user) {
        if (product == null || user == null) {
            throw new IllegalArgumentException("Product and User cannot be null");
        }

        User managedUser = userService.getById(user.getId());
        Set<Product> wishList = managedUser.getWishlist();

        if (wishList == null) {
            wishList = Collections.synchronizedSet(new HashSet<>());
            managedUser.setWishlist(wishList);
        }

        wishList.add(product);
        userService.updateUser(managedUser);
    }

    @Override
    public void addToWishListAll(List<Product> products, User user) {
        if (products == null || user == null || products.isEmpty()) {
            throw new IllegalArgumentException("Product and User cannot be null");
        }

        User managedUser = userService.getById(user.getId());
        Set<Product> wishList = managedUser.getWishlist();

        if (wishList == null) {
            wishList = Collections.synchronizedSet(new HashSet<>());
            managedUser.setWishlist(wishList);
        }

        wishList.addAll(products);
        userService.updateUser(managedUser);
    }

    @Override
    public void removeFromWishList(Long productId, User user) {
        if (productId == null || user == null) {
            throw new IllegalArgumentException("ProductId and User cannot be null");
        }

        Product product = productService.getById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with given ID does not exist");
        }

        User managedUser = userService.getById(user.getId());
        Set<Product> wishList = managedUser.getWishlist();

        if (wishList != null && wishList.remove(product)) {
            userService.updateUser(managedUser);
        }
    }
}
