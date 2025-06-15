package com.example;


import com.example.grocery.entities.GroceryItem;
import com.example.grocery.exception.ResourceNotFoundException;
import com.example.grocery.repository.GroceryItemRepository;
import com.example.grocery.service.implementation.GroceryItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enable Mockito for JUnit 5
public class GroceryItemServiceTest {

    @Mock // Create a mock instance of GroceryItemRepository
    private GroceryItemRepository groceryItemRepository;

    @InjectMocks // Inject the mock(s) into GroceryItemServiceImpl instance
    private GroceryItemServiceImpl groceryItemService;

    // Test data for reuse
    private GroceryItem item1;
    private GroceryItem item2;

    @BeforeEach // This method runs before each test method
    void setUp() {
        // Initialize test data
        item1 = new GroceryItem(1L, "Milk", "Dairy", 2.50, 100);
        item2 = new GroceryItem(2L, "Bread", "Bakery", 3.00, 50);
    }

    // --- Test Cases for getAllGroceryItems() ---   
    @Test
    void getAllGroceryItems_ReturnsListOfItems() {
        // Code Under Test: groceryItemService.getAllGroceryItems()
        // Test case: Verify that getAllGroceryItems returns all items from the repository.
        // Dependency Layer/Function: groceryItemRepository.findAll()
        // Mock Setup: groceryItemRepository.findAll() returns a list of item1 and item2
        when(groceryItemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        List<GroceryItem> items = groceryItemService.getAllGroceryItems();

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(item1, items.get(0));
        assertEquals(item2, items.get(1));
        verify(groceryItemRepository, times(1)).findAll(); // Verify findAll() was called once
    }

    @Test
    void getAllGroceryItems_ReturnsEmptyListWhenNoItems() {
        // Code Under Test: groceryItemService.getAllGroceryItems()
        // Test case: Verify that getAllGroceryItems returns an empty list when no items are in the repository.
        // Mock Setup: groceryItemRepository.findAll() returns an empty list
        when(groceryItemRepository.findAll()).thenReturn(Arrays.asList());

        List<GroceryItem> items = groceryItemService.getAllGroceryItems();

        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(groceryItemRepository, times(1)).findAll();
    }

    // --- Test Cases for getGroceryItemById() ---
    @Test
    void getGroceryItemById_ReturnsItem_WhenFound() {
        // Code Under Test: groceryItemService.getGroceryItemById(id)
        // Test case: Verify that getGroceryItemById returns an item when the ID exists.
        // Mock Setup: groceryItemRepository.findById(1L) returns Optional.of(item1)
        when(groceryItemRepository.findById(1L)).thenReturn(Optional.of(item1));

        Optional<GroceryItem> foundItem = groceryItemService.getGroceryItemById(1L);

        assertTrue(foundItem.isPresent());
        assertEquals(item1, foundItem.get());
        verify(groceryItemRepository, times(1)).findById(1L);
    }

    @Test
    void getGroceryItemById_ReturnsEmptyOptional_WhenNotFound() {
        // Code Under Test: groceryItemService.getGroceryItemById(id)
        // Test case: Verify that getGroceryItemById returns an empty Optional when the ID does not exist.
        // Mock Setup: groceryItemRepository.findById(99L) returns Optional.empty()
        when(groceryItemRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<GroceryItem> foundItem = groceryItemService.getGroceryItemById(99L);

        assertFalse(foundItem.isPresent());
        verify(groceryItemRepository, times(1)).findById(99L);
    }

    // --- Test Cases for createGroceryItem() ---
    @Test
    void createGroceryItem_SavesAndReturnsItem() {
        // Code Under Test: groceryItemService.createGroceryItem(item)
        // Test case: Verify that createGroceryItem saves a new item and returns the saved instance.
        // Mock Setup: groceryItemRepository.save(any(GroceryItem.class)) returns the input item (simulates persistence with ID)
        GroceryItem newItem = new GroceryItem(null, "Eggs", "Dairy", 4.00, 75);
        when(groceryItemRepository.save(any(GroceryItem.class))).thenReturn(item1); // Using item1 as a mock saved result

        GroceryItem createdItem = groceryItemService.createGroceryItem(newItem);

        assertNotNull(createdItem.getId());
        assertEquals(item1.getName(), createdItem.getName());
        assertEquals(item1.getPrice(), createdItem.getPrice());
        verify(groceryItemRepository, times(1)).save(any(GroceryItem.class));
    }

    // --- Test Cases for updateGroceryItem() ---
    @Test
    void updateGroceryItem_UpdatesAndReturnsItem_WhenFound() {
        // Code Under Test: groceryItemService.updateGroceryItem(id, itemDetails)
        // Test case: Verify that updateGroceryItem updates an existing item and returns the updated instance.
        // Mock Setup:
        //   - groceryItemRepository.findById(1L) returns Optional.of(item1)
        //   - groceryItemRepository.save(any(GroceryItem.class)) returns the updated item
        GroceryItem updatedDetails = new GroceryItem(1L, "Milk (Organic)", "Dairy", 3.25, 90);

        when(groceryItemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(groceryItemRepository.save(any(GroceryItem.class))).thenReturn(updatedDetails);

        GroceryItem result = groceryItemService.updateGroceryItem(1L, updatedDetails);

        assertNotNull(result);
        assertEquals(updatedDetails.getName(), result.getName());
        assertEquals(updatedDetails.getPrice(), result.getPrice());
        assertEquals(updatedDetails.getQuantity(), result.getQuantity());
        verify(groceryItemRepository, times(1)).findById(1L);
        verify(groceryItemRepository, times(1)).save(item1); // Verify save was called with the modified original object
    }

    @Test
    void updateGroceryItem_ThrowsResourceNotFoundException_WhenNotFound() {
        // Code Under Test: groceryItemService.updateGroceryItem(id, itemDetails)
        // Test case: Verify that updateGroceryItem throws ResourceNotFoundException when item ID is not found.
        // Mock Setup: groceryItemRepository.findById(99L) returns Optional.empty()
        when(groceryItemRepository.findById(99L)).thenReturn(Optional.empty());

        GroceryItem updatedDetails = new GroceryItem(99L, "Non Existent", "NA", 0.0, 0);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            groceryItemService.updateGroceryItem(99L, updatedDetails);
        });

        assertEquals("GroceryItem not found with id : '99'", exception.getMessage());
        verify(groceryItemRepository, times(1)).findById(99L);
        verify(groceryItemRepository, never()).save(any(GroceryItem.class));
    }

    // --- Test Cases for deleteGroceryItem() ---
    @Test
    void deleteGroceryItem_DeletesItem_WhenFound() {
        // Code Under Test: groceryItemService.deleteGroceryItem(id)
        // Test case: Verify that deleteGroceryItem successfully deletes an item when the ID exists.
        // Mock Setup:
        //   - groceryItemRepository.findById(1L) returns Optional.of(item1)
        //   - groceryItemRepository.delete(item1) performs successfully
        when(groceryItemRepository.findById(1L)).thenReturn(Optional.of(item1));
        doNothing().when(groceryItemRepository).delete(item1);

        groceryItemService.deleteGroceryItem(1L);

        verify(groceryItemRepository, times(1)).findById(1L);
        verify(groceryItemRepository, times(1)).delete(item1);
    }

    @Test
    void deleteGroceryItem_ThrowsResourceNotFoundException_WhenNotFound() {
        // Code Under Test: groceryItemService.deleteGroceryItem(id)
        // Test case: Verify that deleteGroceryItem throws ResourceNotFoundException when item ID is not found.
        // Mock Setup: groceryItemRepository.findById(99L) returns Optional.empty()
        when(groceryItemRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            groceryItemService.deleteGroceryItem(99L);
        });

        assertEquals("GroceryItem not found with id : '99'", exception.getMessage());
        verify(groceryItemRepository, times(1)).findById(99L);
        verify(groceryItemRepository, never()).delete(any(GroceryItem.class));
    }
}
