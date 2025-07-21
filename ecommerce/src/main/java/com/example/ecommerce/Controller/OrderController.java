package com.example.ecommerce.Controller;


import com.example.ecommerce.Entity.Order;
import com.example.ecommerce.Entity.User;
import com.example.ecommerce.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long id) {
        User user = (User) userDetails;
        Order order = orderService.getOrderById(id);

        // Verify the order belongs to the requesting user
        if (!order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok(orderService.createOrderFromCart(user));
    }
}
