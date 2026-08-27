package com.contactmanagement.backend;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.contactmanagement.backend.controller.ContactEmailController;
import com.contactmanagement.backend.dto.ContactEmailRequest;
import com.contactmanagement.backend.entity.ContactEmail;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.service.ContactEmailService;

@ExtendWith(MockitoExtension.class)
class ContactEmailControllerTest {
    @Mock
    private ContactEmailService contactEmailService;

    @InjectMocks
    private ContactEmailController contactEmailController;

    @Test
    void createContactEmailSuccessfully() {
        User user = new User();
        user.setId(10);

        ContactEmailRequest request = new ContactEmailRequest();
        request.setEmailAddress("ali@example.com");
        request.setLabel("Personal");

        ContactEmail createdEmail = new ContactEmail();
        createdEmail.setId(1);
        createdEmail.setEmailAddress("ali@example.com");
        createdEmail.setLabel("Personal");

        when(contactEmailService.createContactEmail(1, 10, request)).thenReturn(createdEmail);

        ResponseEntity<ContactEmail> response = contactEmailController.createContactEmail(1, request, user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdEmail, response.getBody());

        verify(contactEmailService).createContactEmail(1, 10, request);
    }

    @Test
    void getEmailsByContactIdSuccessfully() {
        User user = new User();
        user.setId(10);

        ContactEmail firstEmail = new ContactEmail();
        firstEmail.setId(1);
        firstEmail.setEmailAddress("ali@example.com");
        firstEmail.setLabel("Personal");

        ContactEmail secondEmail = new ContactEmail();
        secondEmail.setId(2);
        secondEmail.setEmailAddress("ali@work.com");
        secondEmail.setLabel("Work");

        List<ContactEmail> emails = List.of(firstEmail, secondEmail);

        when(contactEmailService.getEmailsByContactId(1, 10)).thenReturn(emails);

        List<ContactEmail> result = contactEmailController.getEmailsByContactId(1, user);

        assertEquals(2, result.size());
        assertEquals(emails, result);

        verify(contactEmailService).getEmailsByContactId(1, 10);
    }

    @Test
    void getContactEmailByIdSuccessfully() {
        User user = new User();
        user.setId(10);

        ContactEmail email = new ContactEmail();
        email.setId(1);
        email.setEmailAddress("ali@example.com");
        email.setLabel("Personal");

        when(contactEmailService.getContactEmailById(1, 10)).thenReturn(Optional.of(email));

        ResponseEntity<ContactEmail> response = contactEmailController.getContactEmailById(1, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, response.getBody());

        verify(contactEmailService).getContactEmailById(1, 10);
    }

    @Test
    void deleteContactEmailSuccessfully() {
        User user = new User();
        user.setId(10);

        when(contactEmailService.deleteContactEmail(1, 10)).thenReturn(true);

        ResponseEntity<Void> response = contactEmailController.deleteContactEmail(1, user);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(contactEmailService).deleteContactEmail(1, 10);
    }

    @Test
    void updateContactEmailSuccessfully() { 
        User user = new User();
        user.setId(10);

        ContactEmailRequest request = new ContactEmailRequest();
        request.setEmailAddress("updated@example.com");
        request.setLabel("Work");

        ContactEmail updatedEmail = new ContactEmail();
        updatedEmail.setId(1);
        updatedEmail.setEmailAddress("updated@example.com");
        updatedEmail.setLabel("Work");

        when(contactEmailService.updateContactEmail(1, 10, request)).thenReturn(Optional.of(updatedEmail));

        ResponseEntity<ContactEmail> response = contactEmailController.updateContactEmail(1, request, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEmail, response.getBody());

        verify(contactEmailService).updateContactEmail(1, 10, request);
    }

    @Test
    void getContactEmailByIdReturnsNotFound() { 
        User user = new User();
        user.setId(10);

        when(contactEmailService.getContactEmailById(1, 10)).thenReturn(Optional.empty());

        ResponseEntity<ContactEmail> response = contactEmailController.getContactEmailById(1, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteContactEmailReturnsNotFound() {
        User user = new User();
        user.setId(10);

        when(contactEmailService.deleteContactEmail(1, 10)).thenReturn(false);

        ResponseEntity<Void> response = contactEmailController.deleteContactEmail(1, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
