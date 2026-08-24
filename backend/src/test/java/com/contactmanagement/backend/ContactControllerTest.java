package com.contactmanagement.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.contactmanagement.backend.controller.ContactController;
import com.contactmanagement.backend.dto.ContactRequest;
import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.service.ContactService;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {
    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @Test
    void createContactSuccessfully() {
        User user = new User();
        user.setId(10);

        ContactRequest request = new ContactRequest("Ali", "Khan", "Student");
        Contact savedContact = new Contact("Ali", "Khan", "Student", user);
        savedContact.setId(1);

        when(contactService.saveContact(any(Contact.class))).thenReturn(savedContact);

        ResponseEntity<Contact> response = contactController.createContact(request, user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedContact, response.getBody());

        verify(contactService).saveContact(any(Contact.class));
    }
    
}
