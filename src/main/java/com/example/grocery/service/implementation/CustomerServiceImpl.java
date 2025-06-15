package com.example.grocery.service.implementation;



import com.example.grocery.entities.Customer;
import com.example.grocery.exception.ResourceNotFoundException;
import com.example.grocery.repository.CustomerRepository;
import com.example.grocery.service.CustomerService; // Corrected import
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service component
public class CustomerServiceImpl implements CustomerService {

    @Autowired // Injects CustomerRepository
    private CustomerRepository customerRepository;




    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll(); // Retrieves all customers
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id); // Retrieves a customer by ID
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer); // Saves a new customer
    }

     @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id)); // <-- CHANGE: Throw custom exception
        
        // Update fields from customerDetails
        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        customer.setAddress(customerDetails.getAddress());
        customer.setPhone(customerDetails.getPhone());
        return customerRepository.save(customer);
    }

     @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id)); // <-- CHANGE: Throw custom exception
        customerRepository.delete(customer); // Use delete(entity) instead of deleteById(id) after finding it
    }
}



