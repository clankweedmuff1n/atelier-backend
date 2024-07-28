package com.back.studio.wishlist;

import com.back.studio.auth.responses.ApiResponse;
import com.back.studio.auth.user.User;
import com.back.studio.products.Product.service.ProductService;
import com.back.studio.wishlist.service.WishListService;
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
@RequestMapping("/secured/api/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;
    private final ProductService productService;

    @GetMapping("/add/{productId}")
    public ResponseEntity<ApiResponse> addToWishList(
            HttpServletRequest request,
            @PathVariable String productId
    ) {
        System.out.println("TEST");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        wishListService.addToWishList(productService.getById(Long.valueOf(productId)), user);
        return ResponseEntity.ok(ApiResponse.of(
                new Date(),
                HttpStatus.OK,
                "Successfully added to wishlist",
                request.getRequestURI()
        ));
    }

    @PostMapping("/add/all")
    public ResponseEntity<ApiResponse> addToWishList(
            HttpServletRequest request,
            @RequestBody List<Long> products
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        products.forEach(System.out::println);

        wishListService.addToWishListAll(productService.getAllProductsById(products), user);
        return ResponseEntity.ok(ApiResponse.of(
                new Date(),
                HttpStatus.OK,
                "Successfully added all to wishlist",
                request.getRequestURI()
        ));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse> removeFromWishList(
            HttpServletRequest request,
            @PathVariable String productId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        wishListService.removeFromWishList(Long.valueOf(productId), user);
        return ResponseEntity.ok(ApiResponse.of(
                new Date(),
                HttpStatus.OK,
                "Successfully removed to wishlist",
                request.getRequestURI()
        ));
    }
}
