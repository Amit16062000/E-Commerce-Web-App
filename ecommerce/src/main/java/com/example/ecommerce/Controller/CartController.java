package com.example.ecommerce.Controller;



import com.example.ecommerce.Entity.User;
import com.example.ecommerce.dto.CartItemDto;
import com.example.ecommerce.Entity.Cart;
import com.example.ecommerce.Service.CartService;
import com.example.ecommerce.dto.CartResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        Cart cart = cartService.getCartByUser(user);
        return ResponseEntity.ok(CartResponseDto.fromCart(cart));
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody CartItemDto cartItemDto) {
        User user = (User) userDetails;
        return ResponseEntity.ok(cartService.addItemToCart(user, cartItemDto));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Cart> updateCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long itemId,
                                               @RequestParam int quantity) {
        User user = (User) userDetails;
        return ResponseEntity.ok(cartService.updateCartItem(user, itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable Long itemId) {
        User user = (User) userDetails;
        cartService.removeItemFromCart(user, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
}
