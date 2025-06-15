package com.example.grocery.service;

import com.example.grocery.entities.GroceryItem;
import java.util.List;
import java.util.Optional;

public interface GroceryItemService {
    List<GroceryItem> getAllGroceryItems();
    Optional<GroceryItem> getGroceryItemById(Long id);
    GroceryItem createGroceryItem(GroceryItem groceryItem);
    GroceryItem updateGroceryItem(Long id, GroceryItem groceryItemDetails);
    void deleteGroceryItem(Long id);
}