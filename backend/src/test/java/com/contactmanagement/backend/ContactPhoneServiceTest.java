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

import com.contactmanagement.backend.dto.ContactPhoneRequest;
import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.entity.ContactPhone;
import com.contactmanagement.backend.repository.ContactPhoneRepository;
import com.contactmanagement.backend.service.ContactPhoneService;
import com.contactmanagement.backend.service.ContactService;

@ExtendWith(MockitoExtension.class)
public class ContactPhoneServiceTest {
    @Mock
    private ContactPhoneRepository contactPhoneRepository;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactPhoneService contactPhoneService;

    //Test: create contact phone
    @Test
    void createContactPhoneSuccessfully() {

        Contact contact = new Contact();
        contact.setId(1);

        ContactPhoneRequest request = new ContactPhoneRequest();
        request.setPhoneNumber("03001234567");
        request.setLabel("Mobile");

        when(contactService.getContactById(1, 10)).thenReturn(Optional.of(contact));

        ContactPhone savedPhone = new ContactPhone("03001234567", "Mobile", contact);

        savedPhone.setId(5);

        when(contactPhoneRepository.save(any(ContactPhone.class))).thenReturn(savedPhone);

        ContactPhone result = contactPhoneService.createContactPhone(1, 10, request);

        assertEquals("03001234567", result.getPhoneNumber());
        assertEquals("Mobile", result.getLabel());

        verify(contactService).getContactById(1, 10);
        verify(contactPhoneRepository).save(any(ContactPhone.class));
    }

    //Test: failed to create contact phone
    @Test
    void createContactPhoneFailsWhenContactNotFound() {

        ContactPhoneRequest request = new ContactPhoneRequest();
        request.setPhoneNumber("03001234567");
        request.setLabel("Mobile");

        when(contactService.getContactById(1, 10)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> contactPhoneService.createContactPhone(1, 10, request));

        verify(contactService).getContactById(1, 10);
    }
    
}
