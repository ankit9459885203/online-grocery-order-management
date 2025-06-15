package com.example.grocery.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus annotation tells Spring to return a 404 Not Found status when this exception is thrown
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    // Constructor for when a resource is not found by a specific field (e.g., ID)
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // Construct the detailed error message
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    // You can add additional constructors or custom messages if needed
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Getters for additional details (optional, but good for debugging/logging)
    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}