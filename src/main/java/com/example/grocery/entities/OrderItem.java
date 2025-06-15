package com.example.grocery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "order item")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    // Many-to-One relationship with Order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
private Order order;

    // Many-to-One relationship with GroceryItem
    @ManyToOne
    @JoinColumn(name = "grocery_item_id", nullable = false)
private GroceryItem groceryItem;

private int quantity; // Quantity of this specific grocery item in the order
private double unitPrice; // Price of the item at the time of order
}

