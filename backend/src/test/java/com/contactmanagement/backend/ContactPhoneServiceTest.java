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

    //Test: Delete contact phone
    @Test
    void deleteContactPhoneSuccessfully() {

        Contact contact = new Contact();
        contact.setId(1);

        ContactPhone contactPhone = new ContactPhone("03001234567", "Mobile", contact);
        contactPhone.setId(5);

        when(contactPhoneRepository.findById(5)).thenReturn(Optional.of(contactPhone));
        when(contactService.getContactById(1, 10)).thenReturn(Optional.of(contact));

        boolean result = contactPhoneService.deleteContactPhone(5, 10);
        assertTrue(result);

        verify(contactPhoneRepository).deleteById(5);
    }

    @Test
    void getPhonesByContactIdSuccessfully() {

        Contact contact = new Contact();
        contact.setId(1);

        ContactPhone phone = new ContactPhone("03001234567", "Mobile", contact);
        phone.setId(5);

        when(contactService.getContactById(1, 10)).thenReturn(Optional.of(contact));
        when(contactPhoneRepository.findByContactId(1)).thenReturn(List.of(phone));

        List<ContactPhone> result = contactPhoneService.getPhonesByContactId(1, 10);

        assertEquals(1, result.size());
        assertEquals(phone, result.get(0));

        verify(contactService).getContactById(1, 10);
        verify(contactPhoneRepository).findByContactId(1);
    }

    @Test
    void updateContactPhoneSuccessfully() {
        Contact contact = new Contact();
        contact.setId(1);

        ContactPhone existingPhone = new ContactPhone("03001234567", "Mobile", contact);
        existingPhone.setId(5);

        ContactPhoneRequest request = new ContactPhoneRequest();
        request.setPhoneNumber("03111234567");
        request.setLabel("Work");

        when(contactPhoneRepository.findById(5)).thenReturn(Optional.of(existingPhone));
        when(contactService.getContactById(1, 10)).thenReturn(Optional.of(contact));
        when(contactPhoneRepository.save(existingPhone)).thenReturn(existingPhone);

        Optional<ContactPhone> result = contactPhoneService.updateContactPhone(5, 10, request);

        assertTrue(result.isPresent());
        assertEquals("03111234567", result.get().getPhoneNumber());
        assertEquals("Work", result.get().getLabel());

        verify(contactPhoneRepository).save(existingPhone);
    }
    
}
