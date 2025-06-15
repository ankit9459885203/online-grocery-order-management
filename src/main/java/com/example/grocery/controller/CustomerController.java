package com.example.grocery.controller;

import com.example.grocery.entities.Customer;
import com.example.grocery.service.CustomerService; // Corrected import

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;









 
@RestController // Marks this class as a REST controller
@RequestMapping("/customers") // Base path for all endpoints in this controller
public class CustomerController {

    @Autowired // Injects CustomerService
    private CustomerService customerService;



    
    // GET /customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // GET /customers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Will 
    }

    // POST /customers
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    // PUT /customers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,@Valid @RequestBody Customer customerDetails) {
           Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }
    

    // DELETE /customers/{id}
     @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        // <-- CHANGE: Removed try-catch. GlobalExceptionHandler will catch ResourceNotFoundException.
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}