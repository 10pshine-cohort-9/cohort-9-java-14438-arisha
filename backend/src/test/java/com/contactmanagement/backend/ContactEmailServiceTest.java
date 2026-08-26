package com.contactmanagement.backend;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    //Test: Delete email of a registered user
    @Test
    void deleteContactEmailSuccessfully() {
        
        Contact contact = new Contact();
        contact.setId(1);

        ContactEmail contactEmail = new ContactEmail("arisha@example.com", "Personal", contact);
        contactEmail.setId(5);

        when(contactEmailRepository.findById(5)).thenReturn(Optional.of(contactEmail));
        when(contactService.getContactById(1, 10)).thenReturn(Optional.of(contact));

        boolean result = contactEmailService.deleteContactEmail(5, 10);

        assertTrue(result);

        verify(contactEmailRepository).deleteById(5);
    }
    
    @Test
    void getEmailsByContactIdSuccessfully() {
        Contact contact = new Contact();
        contact.setId(1);

        ContactEmail email = new ContactEmail("arisha@example.com", "Personal", contact);
        email.setId(5);

        when(contactService.getContactById(1, 10)).thenReturn(Optional.of(contact));
        when(contactEmailRepository.findByContactId(1)).thenReturn(List.of(email));

        List<ContactEmail> result = contactEmailService.getEmailsByContactId(1, 10);

        assertEquals(1, result.size());
        assertEquals(email, result.get(0));

        verify(contactService).getContactById(1, 10);
        verify(contactEmailRepository).findByContactId(1);
    }

    @Test
    void updateContactEmailSuccessfully() {
        Contact contact = new Contact();
        contact.setId(1);

        ContactEmail existingEmail = new ContactEmail("old@example.com", "Personal", contact);
        existingEmail.setId(5);

        ContactEmailRequest request = new ContactEmailRequest();
        request.setEmailAddress("new@example.com");
        request.setLabel("Work");

        when(contactEmailRepository.findById(5)).thenReturn(Optional.of(existingEmail));
        when(contactService.getContactById(1, 10)).thenReturn(Optional.of(contact));
        when(contactEmailRepository.save(existingEmail)).thenReturn(existingEmail);

        Optional<ContactEmail> result = contactEmailService.updateContactEmail(5, 10, request);

        assertTrue(result.isPresent());
        assertEquals("new@example.com", result.get().getEmailAddress());
        assertEquals("Work", result.get().getLabel());

        verify(contactEmailRepository).save(existingEmail);
    }
}
