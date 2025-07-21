package com.example.ecommerce.dto;


import com.example.ecommerce.Entity.CartItem;
import com.example.ecommerce.Entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponseDto {
    private Long id;
    private User user;
    private List<CartItemDto> items;
    private double totalPrice;

    @Data
    public static class CartItemDto {
        private Long id;
        private ProductDto product;
        private int quantity;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public ProductDto getProduct() {
            return product;
        }

        public void setProduct(ProductDto product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    @Data
    public static class ProductDto {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        private double price;
        // Only include what you want to expose
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }


    // Helper method to convert from Cart entity
    public static CartResponseDto fromCart(com.example.ecommerce.Entity.Cart cart) {
        CartResponseDto dto = new CartResponseDto();
        dto.setId(cart.getId());
        dto.setUser(cart.getUser());
        dto.setTotalPrice(cart.getTotalPrice());

        dto.setItems(cart.getItems().stream().map(item -> {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setId(item.getId());
            itemDto.setQuantity(item.getQuantity());

            ProductDto productDto = new ProductDto();
            productDto.setId(item.getProduct().getId());
            productDto.setName(item.getProduct().getName());
            productDto.setPrice(item.getProduct().getPrice());

            itemDto.setProduct(productDto);
            return itemDto;
        }).toList());

        return dto;
    }
}
