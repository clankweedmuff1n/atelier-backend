package com.back.studio.cart;

import com.back.studio.auth.responses.ApiResponse;
import com.back.studio.auth.user.User;
import com.back.studio.cart.service.CartService;
import com.back.studio.products.Product.Product;
import com.back.studio.products.Product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/secured/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/add/{productId}")
    public ResponseEntity<ApiResponse> addToCart(
            HttpServletRequest request,
            @PathVariable String productId
    ) {
        System.out.println("TEST");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        cartService.addToCart(productService.getById(Long.valueOf(productId)), user);
        return ResponseEntity.ok(ApiResponse.of(
                new Date(),
                HttpStatus.OK,
                "Successfully added to cart",
                request.getRequestURI()
        ));
    }

    @PostMapping("/add/all")
    public ResponseEntity<ApiResponse> addToCart(
            HttpServletRequest request,
            @RequestBody List<Long> products
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        products.forEach(System.out::println);

        cartService.addToCartAll(productService.getAllProductsById(products), user);
        return ResponseEntity.ok(ApiResponse.of(
                new Date(),
                HttpStatus.OK,
                "Successfully added all to cart",
                request.getRequestURI()
        ));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse> removeFromCart(
            HttpServletRequest request,
            @PathVariable String productId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        cartService.removeFromCart(Long.valueOf(productId), user);
        return ResponseEntity.ok(ApiResponse.of(
                new Date(),
                HttpStatus.OK,
                "Successfully removed to cart",
                request.getRequestURI()
        ));
    }
}
