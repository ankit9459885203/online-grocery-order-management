package com.example.grocery.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="grocery_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryItem {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

private String  name;
private String  category;

 @NotNull(message = "Price cannot be null")// valiatioin test
private double  price;
private int  quantity;


    
}
