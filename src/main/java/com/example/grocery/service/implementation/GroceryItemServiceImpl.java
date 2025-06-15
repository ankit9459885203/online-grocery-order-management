package com.example.grocery.service.implementation;

import com.example.grocery.entities.GroceryItem;
import com.example.grocery.exception.ResourceNotFoundException;
import com.example.grocery.repository.GroceryItemRepository;
import com.example.grocery.service.GroceryItemService; // Corrected import
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroceryItemServiceImpl implements GroceryItemService {

    @Autowired
    private GroceryItemRepository groceryItemRepository;

    @Override
    public List<GroceryItem> getAllGroceryItems() {
        return groceryItemRepository.findAll();
    }

    @Override
    public Optional<GroceryItem> getGroceryItemById(Long id) {
        return groceryItemRepository.findById(id);
    }

    @Override
    public GroceryItem createGroceryItem(GroceryItem groceryItem) {
        return groceryItemRepository.save(groceryItem);
    }

    @Override
    public GroceryItem updateGroceryItem(Long id, GroceryItem groceryItemDetails) {
        GroceryItem item = groceryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("GroceryItem", "id", id)); // <-- CHANGE: Throw custom exception
        
        item.setName(groceryItemDetails.getName());
        item.setCategory(groceryItemDetails.getCategory());
        item.setPrice(groceryItemDetails.getPrice());
        item.setQuantity(groceryItemDetails.getQuantity());
        return groceryItemRepository.save(item);
    }

    @Override
    public void deleteGroceryItem(Long id) {
        GroceryItem item = groceryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("GroceryItem", "id", id)); // <-- CHANGE: Throw custom exception
        groceryItemRepository.delete(item); // Use delete(entity) instead of deleteById(id)
    }
}