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

import com.contactmanagement.backend.controller.ContactPhoneController;
import com.contactmanagement.backend.dto.ContactPhoneRequest;
import com.contactmanagement.backend.entity.ContactPhone;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.service.ContactPhoneService;

@ExtendWith(MockitoExtension.class)
public class ContactPhoneControllerTest {
    @Mock
    private ContactPhoneService contactPhoneService;

    @InjectMocks
    private ContactPhoneController contactPhoneController;

    @Test
    void createContactPhoneSuccessfully() {
        User user = new User();
        user.setId(10);

        ContactPhoneRequest request = new ContactPhoneRequest();
        request.setPhoneNumber("03001234567");
        request.setLabel("Personal");

        ContactPhone createdPhone = new ContactPhone();
        createdPhone.setId(1);
        createdPhone.setPhoneNumber("03001234567");
        createdPhone.setLabel("Personal");

        when(contactPhoneService.createContactPhone(1, 10, request)).thenReturn(createdPhone);

        ResponseEntity<ContactPhone> response = contactPhoneController.createContactPhone(1, request, user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdPhone, response.getBody());

        verify(contactPhoneService).createContactPhone(1, 10, request);
    }
    
}
