package com.example.grocery.controller;

import com.example.grocery.entities.Order;
import com.example.grocery.service.OrderService; // Corrected import

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // GET /orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // GET /orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST /orders
       @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // This catch block is still here because of the "Not enough stock" RuntimeException.
            // In the future, you might want a specific `InsufficientStockException`
            // handled by GlobalExceptionHandler with a 409 Conflict status.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT /orders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody Order orderDetails) {
        // <-- CHANGE: Removed try-catch. GlobalExceptionHandler will catch ResourceNotFoundException.
        Order updatedOrder = orderService.updateOrder(id, orderDetails);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
    

    // DELETE /orders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        // <-- CHANGE: Removed try-catch. GlobalExceptionHandler will catch ResourceNotFoundException.
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
