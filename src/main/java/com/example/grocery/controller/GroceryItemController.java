package com.example.grocery.controller;

import com.example.grocery.entities.GroceryItem;
import com.example.grocery.service.GroceryItemService;

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grocery-items")
public class GroceryItemController {

    @Autowired
    private GroceryItemService groceryItemService;

    // GET /grocery-items
    @GetMapping
    public ResponseEntity<List<GroceryItem>> getAllGroceryItems() {
        List<GroceryItem> items = groceryItemService.getAllGroceryItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // GET /grocery-items/{id}
    @GetMapping("/{id}")
    public ResponseEntity<GroceryItem> getGroceryItemById(@PathVariable Long id) {
        return groceryItemService.getGroceryItemById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST /grocery-items
    @PostMapping
    public ResponseEntity<GroceryItem> createGroceryItem(@RequestBody GroceryItem groceryItem) {
        GroceryItem createdItem = groceryItemService.createGroceryItem(groceryItem);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    // PUT /grocery-items/{id}
   @PutMapping("/{id}")
    public ResponseEntity<GroceryItem> updateGroceryItem(@PathVariable Long id, @Valid @RequestBody GroceryItem groceryItemDetails) {
        // <-- CHANGE: Removed try-catch. GlobalExceptionHandler will catch ResourceNotFoundException.
        GroceryItem updatedItem = groceryItemService.updateGroceryItem(id, groceryItemDetails);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    // DELETE /grocery-items/{id}
     @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroceryItem(@PathVariable Long id) {
        // <-- CHANGE: Removed try-catch. GlobalExceptionHandler will catch ResourceNotFoundException.
        groceryItemService.deleteGroceryItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}