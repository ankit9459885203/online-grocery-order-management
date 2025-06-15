package com.example.grocery.service.implementation;

import com.example.grocery.exception.ResourceNotFoundException;
import com.example.grocery.entities.Customer;
import com.example.grocery.entities.GroceryItem;
import com.example.grocery.entities.Order;
import com.example.grocery.entities.OrderItem;
import com.example.grocery.repository.CustomerRepository;
import com.example.grocery.repository.GroceryItemRepository;
import com.example.grocery.repository.OrderRepository;
import com.example.grocery.service.OrderService; // Corrected import
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For transactional operations

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GroceryItemRepository groceryItemRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        // Ensure customer exists
        Customer customer = customerRepository.findById(order.getCustomer().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", order.getCustomer().getId())); // <-- CHANGE: Throw custom exception
        order.setCustomer(customer);

        double totalOrderPrice = 0.0;
        // Process each OrderItem, link to order, fetch actual item details, and update stock
        for (OrderItem item : order.getOrderItems()) {
            GroceryItem groceryItem = groceryItemRepository.findById(item.getGroceryItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("GroceryItem", "id", item.getGroceryItem().getId())); // <-- CHANGE: Throw custom exception

            if (groceryItem.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for item: " + groceryItem.getName()); // <-- KEEP as RuntimeException for now, or define a custom for stock.
            }

            // Update stock
            groceryItem.setQuantity(groceryItem.getQuantity() - item.getQuantity());
            groceryItemRepository.save(groceryItem); // Save updated stock

            item.setGroceryItem(groceryItem); // Link actual item object
            item.setOrder(order); // Link back to the order
            item.setUnitPrice(groceryItem.getPrice()); // Set unit price at time of order
            totalOrderPrice += item.getQuantity() * item.getUnitPrice();
        }

        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(totalOrderPrice);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id)); // <-- CHANGE: Throw custom exception

        // Handle customer update if provided
        if (orderDetails.getCustomer() != null && orderDetails.getCustomer().getId() != null) {
            Customer customer = customerRepository.findById(orderDetails.getCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", orderDetails.getCustomer().getId())); // <-- CHANGE: Throw custom exception
            existingOrder.setCustomer(customer);
        }

        // This update method is simplified; a full update would require complex logic
        // to handle additions/deletions/modifications of order items,
        // including reverting/updating stock quantities.
        existingOrder.setOrderDate(orderDetails.getOrderDate() != null ? orderDetails.getOrderDate() : existingOrder.getOrderDate());
        existingOrder.setTotalPrice(orderDetails.getTotalPrice());

        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order orderToDelete = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id)); // <-- CHANGE: Throw custom exception
        
        // Revert stock for items in the order before deleting
        for (OrderItem item : orderToDelete.getOrderItems()) {
            GroceryItem groceryItem = item.getGroceryItem();
            if (groceryItem != null) { // Add null check for safety
                 groceryItem.setQuantity(groceryItem.getQuantity() + item.getQuantity());
                 groceryItemRepository.save(groceryItem);
            }
        }
        orderRepository.delete(orderToDelete); // Use delete(entity)
    }
}
