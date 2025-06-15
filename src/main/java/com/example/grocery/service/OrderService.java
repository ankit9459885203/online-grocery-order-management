package com.example.grocery.service;

import com.example.grocery.entities.Order;
// import com.example.grocery.entities.OrderItem;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Order createOrder(Order order); // This will need to handle OrderItems correctly
    Order updateOrder(Long id, Order orderDetails);
    void deleteOrder(Long id);
}