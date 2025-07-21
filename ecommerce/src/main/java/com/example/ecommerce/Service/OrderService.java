package com.example.ecommerce.Service;


import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.dto.OrderItemDto;
import com.example.ecommerce.Entity.*;
import com.example.ecommerce.Repository.OrderRepository;
import com.example.ecommerce.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order createOrderFromCart(User user) {
        Cart cart = cartService.getCartByUser(user);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot create order - cart is empty");
        }

        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PROCESSING");

        // Convert cart items to order items
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // Calculate total
        double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);

        // Update product stock
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            int newStock = product.getStockQuantity() - item.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException(
                        "Not enough stock for product: " + product.getName());
            }
            product.setStockQuantity(newStock);
            productRepository.save(product);
        }

        // Clear the cart
        cartService.clearCart(user);

        // Save the order
        return orderRepository.save(order);
    }
}