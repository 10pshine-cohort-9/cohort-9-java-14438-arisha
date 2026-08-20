package com.contactmanagement.backend;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.repository.ContactRepository;
import com.contactmanagement.backend.service.ContactService;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    //Case 1: Successfully getting correct contact by id for correct user
    @Test
    void getContactByIdSuccessfully() {

        //Give sample contact details
        Contact contact = new Contact();
        contact.setId(1);
        contact.setFirstName("Ali");
        contact.setLastName("Khan");

        when(contactRepository.findByIdAndUserId(1, 10)).thenReturn(Optional.of(contact));
        Optional<Contact> result = contactService.getContactById(1, 10); // call service

        //Returned values
        assertTrue(result.isPresent());
        assertEquals("Ali", result.get().getFirstName());
        assertEquals("Khan", result.get().getLastName());

        verify(contactRepository).findByIdAndUserId(1, 10);
    } 

    //Case 2: Find and delete contact
    @Test
    void deleteContactSuccessfully() {

        //Give contact id for deletion
        Contact contact = new Contact();
        contact.setId(1);

        when(contactRepository.findByIdAndUserId(1, 10)).thenReturn(Optional.of(contact));
        contactService.deleteContact(1, 10);

        verify(contactRepository).findByIdAndUserId(1, 10);
        verify(contactRepository).delete(contact);
    }
    
}
