package com.bankapplication.BankRestApi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
//import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long customerId;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must not contain special characters or numbers")
    private String name;

    @Column(name = "pan", nullable = false, length = 20)
    @NotBlank(message = "PAN is required")
    @Size(min = 8, message = "PAN must be at least 8 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "PAN must not contain special characters")
    private String pan;

    @Column(length = 100)
    @Email(message = "Invalid email format")
    private String email;

    @Column(length = 20)
    @Pattern(regexp = "^(?:\\+91[\\-\\s]?|0)?[6-9]\\d{9}$", message = "Phone must be a valid Indian number")
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   // @Column(name = "accounts")
    @JsonManagedReference
    private List<BankAccount> accounts;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now(); updatedAt = createdAt;
    }
    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
