package com.contactmanagement.backend;

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
public class ContactEmailControllerTest {
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
}
