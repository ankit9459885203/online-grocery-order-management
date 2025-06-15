package com.example.grocery.entities;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
   
// hey between order and Grocery item we have many to many relation so we need to make a third table and here i will create field of that talbe


@Entity
@Table(name= "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id; 

       @ManyToOne
       @JoinColumn(name= "customer_id"  , nullable =  false)  // mtlv this id cant be null
private Customer customer;// customer is object also thats why declaring relation here 
                           // here the foriegn key column will be created on current class side(Order.java) , if dont want to do that use "mapped by"

       @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
private List<OrderItem> orderItems = new ArrayList<>(); // Initialize to prevent NullPointerExceptions
// in manyToOne  we dont use  mappedBy , it is used ony in oneToMany
// mapped by means  ki ye vali class foreing key hold ni kregi, but dusri vali classs kregi or uski order field hogi vo.
// cascade simply means ki agar me parent class pr koi crud operation krunga toh changes udhr  newly created 3rd class me v hone
// orphanRmoval -  agar me parent class se kuch delete krta toh vo database se v remove hoga.

private LocalDateTime  orderDate;
private double totalPrice;



   // Helper method to add an OrderItem to the order, ensuring bi-directional relationship is maintained
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);  
        orderItem.setOrder(this); // this keyword refers to recently creted object of order  - Order order = new Order();  
    }

    // Helper method to remove an OrderItem from the order
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);    // 1. Removes from Order's list (Order sid
        orderItem.setOrder(null);  // 2. Nullifies back-reference (OrderItem side)
    }





}