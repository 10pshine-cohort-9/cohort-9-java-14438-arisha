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
@Table(name= "contacts")
public class Contact {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name= "first_name", nullable= false, length= 50)
    private String firstName;

    @Column(name= "last_name", nullable= false, length= 50)
    private String lastName;

    @Column(name= "title", length= 100)
    private String title;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable= false)
    private User user;

    public Contact() {
    }

    public Contact(String firstName, String lastName, String title, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
