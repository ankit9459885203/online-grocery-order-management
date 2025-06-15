package com.example.grocery.entities;


import jakarta.persistence.*;// for @id  & GenratedValue.
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="customer")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Customer {

    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREASE ID
private Long id;  


     @NotNull //validaion ensures the filed is not null
    private String name;

    @Email (message = "email can not be null") // validation Checks if a String field is a well-formed email address.
     private String email;
      private String address;
       private String phone;
    
    
}

/*What Happens If You Forget @Entity?
❌ JPA Will Ignore the Class → No table will be created.
❌ Repositories Won’t Work → customerRepository.save() will fail.
❌ Relationships Break → @ManyToOne/@OneToMany will cause errors */


/*  @Id   :Primary Key Requirement in Databases
Every database table must have a primary key to uniquely identify each row.
Even if you don't explicitly define relationships (@OneToMany, @ManyToOne), JPA still needs a primary key (@Id) to manage the entity. */