package com.contactmanagement.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name= "users")
public class User {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) 
    private Integer id;

    @Column (name= "full_name", nullable= false, length= 100)
    private String fullName;

    @Column (name= "email", unique=true, length = 255)
    private String email;

    @Column (name = "phone_number", unique= true, length = 20)
    private String phoneNumber;

    @Column (name = "password_hash", nullable = false, length = 100)
    private String passwordHash; 

    public User() {
        
    }

    public User(String fullName,String email, String phoneNumber, String passwordHash) {
        this.email = email;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
