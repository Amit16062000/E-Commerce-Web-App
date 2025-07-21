package com.example.ecommerce.Service;


import com.example.ecommerce.dto.CartItemDto;
import com.example.ecommerce.Entity.*;
import com.example.ecommerce.Repository.CartRepository;
import com.example.ecommerce.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalPrice(0.0);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addItemToCart(User user, CartItemDto cartItemDto) {
        Cart cart = getCartByUser(user);
        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity if product already in cart
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItemDto.getQuantity());
        } else {
            // Add new item to cart
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(cartItemDto.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        updateCartTotalPrice(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCartItem(User user, Long itemId, int quantity) {
        Cart cart = getCartByUser(user);
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(quantity);
        updateCartTotalPrice(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public void removeItemFromCart(User user, Long itemId) {
        Cart cart = getCartByUser(user);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        updateCartTotalPrice(cart);
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

    private void updateCartTotalPrice(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(total);
    }
}