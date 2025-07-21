package com.example.ecommerce.dto;

import java.util.List;

public class OrderDto {
    private List<OrderItemDto> items;

    // Getters and setters
    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}