package com.contactmanagement.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "contact_phones")
public class ContactPhone {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name= "phone_number", nullable= false, length= 20)
    private String phoneNumber;

    @Column(name= "label", nullable= false, length= 50)
    private String label;

    @ManyToOne
    @JoinColumn(name= "contact_id", nullable= false)
    private Contact contact;

    public ContactPhone() {
    }

    public ContactPhone(String phoneNumber, String label, Contact contact) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label is required");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact is required");
        }
        this.phoneNumber = phoneNumber;
        this.label = label;
        this.contact = contact;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label is required");
        }
        this.label = label;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact is required");
        }
        this.contact = contact;
    }
    
}
