package com.contactmanagement.backend;

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

import com.contactmanagement.backend.controller.ContactPhoneController;
import com.contactmanagement.backend.dto.ContactPhoneRequest;
import com.contactmanagement.backend.entity.ContactPhone;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.service.ContactPhoneService;

@ExtendWith(MockitoExtension.class)
class ContactPhoneControllerTest {
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

    @Test
    void getPhoneByIdSuccessfully() {
        User user = new User();
        user.setId(10);

        ContactPhone phone = new ContactPhone();
        phone.setId(1);
        phone.setPhoneNumber("03001234567");
        phone.setLabel("Personal");

        when(contactPhoneService.getContactPhoneById(1, 10)).thenReturn(Optional.of(phone));

        ResponseEntity<ContactPhone> response = contactPhoneController.getPhoneById(1, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(phone, response.getBody());

        verify(contactPhoneService).getContactPhoneById(1, 10);
    }

    @Test
    void updateContactPhoneSuccessfully() {
        User user = new User();
        user.setId(10);

        ContactPhoneRequest request = new ContactPhoneRequest();
        request.setPhoneNumber("03111234567");
        request.setLabel("Work");

        ContactPhone updatedPhone = new ContactPhone();
        updatedPhone.setId(1);
        updatedPhone.setPhoneNumber("03111234567");
        updatedPhone.setLabel("Work");

        when(contactPhoneService.updateContactPhone(1, 10, request)).thenReturn(Optional.of(updatedPhone));

        ResponseEntity<ContactPhone> response = contactPhoneController.updateContactPhone(1, request, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPhone, response.getBody());

        verify(contactPhoneService).updateContactPhone(1, 10, request);
    }

    @Test
    void deleteContactPhoneSuccessfully() {
        User user = new User();
        user.setId(10);

        when(contactPhoneService.deleteContactPhone(1, 10)).thenReturn(true);

        ResponseEntity<Void> response = contactPhoneController.deleteContactPhone(1, user);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(contactPhoneService).deleteContactPhone(1, 10);
    }

    @Test
    void getPhoneByIdReturnsNotFound() {
        User user = new User();
        user.setId(10);

        when(contactPhoneService.getContactPhoneById(1, 10)).thenReturn(Optional.empty());

        ResponseEntity<ContactPhone> response = contactPhoneController.getPhoneById(1, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateContactPhoneReturnsNotFound() {
        User user = new User();
        user.setId(10);

        ContactPhoneRequest request = new ContactPhoneRequest();

        when(contactPhoneService.updateContactPhone(1, 10, request)).thenReturn(Optional.empty());

        ResponseEntity<ContactPhone> response = contactPhoneController.updateContactPhone(1, request, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteContactPhoneReturnsNotFound() {

        User user = new User();
        user.setId(10);

        when(contactPhoneService.deleteContactPhone(1, 10)).thenReturn(false);

        ResponseEntity<Void> response = contactPhoneController.deleteContactPhone(1, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
}
