package com.contactmanagement.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.contactmanagement.backend.dto.ContactRequest;

class ContactRequestTest {

    @Test
    void contactRequestGettersAndSettersWork() {

        ContactRequest request = new ContactRequest();

        request.setFirstName("Ali");
        request.setLastName("Khan");
        request.setTitle("Student");

        assertEquals("Ali", request.getFirstName());
        assertEquals("Khan", request.getLastName());
        assertEquals("Student", request.getTitle());
    }

    @Test
    void contactRequestConstructorWorks() {

        ContactRequest request =
                new ContactRequest("Sara", "Ahmed", "Developer");

        assertEquals("Sara", request.getFirstName());
        assertEquals("Ahmed", request.getLastName());
        assertEquals("Developer", request.getTitle());
        
    }
}
