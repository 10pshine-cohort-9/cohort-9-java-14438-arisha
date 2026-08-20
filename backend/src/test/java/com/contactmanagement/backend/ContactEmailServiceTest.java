package com.contactmanagement.backend;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.contactmanagement.backend.dto.ContactEmailRequest;
import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.entity.ContactEmail;
import com.contactmanagement.backend.repository.ContactEmailRepository;
import com.contactmanagement.backend.service.ContactEmailService;
import com.contactmanagement.backend.service.ContactService;

@ExtendWith(MockitoExtension.class)
public class ContactEmailServiceTest {
    @Mock
    private ContactEmailRepository contactEmailRepository;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactEmailService contactEmailService;

    //Test: Create an email successfully
    @Test
    void createContactEmailSuccessfully() {
        //create email for a id
        Contact contact = new Contact();
        contact.setId(1);
        //set new email and label
        ContactEmailRequest request = new ContactEmailRequest();
        request.setEmailAddress("arisha@example.com");
        request.setLabel("Personal");

        when(contactService.getContactById(1, 10)).thenReturn(Optional.of(contact));

        ContactEmail savedEmail = new ContactEmail("arisha@example.com", "Personal", contact);

        savedEmail.setId(5);

        when(contactEmailRepository.save(any(ContactEmail.class))).thenReturn(savedEmail);

        ContactEmail result = contactEmailService.createContactEmail(1, 10, request);

        assertEquals("arisha@example.com", result.getEmailAddress());
        assertEquals("Personal", result.getLabel());

        verify(contactService).getContactById(1, 10);
        verify(contactEmailRepository).save(any(ContactEmail.class));
    }

    //Test: Email creation fails because contact does not exist
    @Test
    void createContactEmailFailsWhenContactNotFound() {

        ContactEmailRequest request = new ContactEmailRequest();
        request.setEmailAddress("arisha@example.com");
        request.setLabel("Personal");
        // email created for id 1 that does not exist
        when(contactService.getContactById(1, 10)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> contactEmailService.createContactEmail(1, 10, request));

        verify(contactService).getContactById(1, 10);
    }
    
}
