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
@Table (name= "contact_emails") 
public class ContactEmail {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name= "email_address", nullable= false, length= 255)
    private String emailAddress;

    @Column(name= "label", nullable= false, length= 50)
    private String label;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable= false)
    private Contact contact;

    public ContactEmail() {
    }

    public ContactEmail(String emailAddress, String label, Contact contact) {

    if (emailAddress == null || emailAddress.isBlank()) {
        throw new IllegalArgumentException("Email address is required");
    }
    if (label == null || label.isBlank()) {
        throw new IllegalArgumentException("Label is required");
    }
    if (contact == null) {
        throw new IllegalArgumentException("Contact is required");
    }

    this.emailAddress = emailAddress;
    this.label = label;
    this.contact = contact;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.isBlank()) {
        throw new IllegalArgumentException("Email address is required");
        }
        this.emailAddress = emailAddress;
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
