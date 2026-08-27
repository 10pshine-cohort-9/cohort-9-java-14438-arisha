package com.contactmanagement.backend;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.entity.User;
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

    //Case 3: The contact does not exist for users to delete
    @Test
    void deleteContactFailsWhenContactNotFound() {
        //contact not found
        when(contactRepository.findByIdAndUserId(1, 10)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> contactService.deleteContact(1, 10));

        verify(contactRepository).findByIdAndUserId(1, 10);
    }

    //Case 4: Update a contact for a registered user
    @Test
    void updateContactSuccessfully() {

        //old contact details
        Contact existingContact = new Contact();
        existingContact.setId(1);
        existingContact.setFirstName("Ali");
        existingContact.setLastName("Khan");
        existingContact.setTitle("Student");

        //new contact details
        Contact updatedContact = new Contact();
        updatedContact.setFirstName("Arisha");
        updatedContact.setLastName("Fatima");
        updatedContact.setTitle("Student");

        when(contactRepository.findByIdAndUserId(1, 10)).thenReturn(Optional.of(existingContact));
        when(contactRepository.save(existingContact)).thenReturn(existingContact);

        Contact result = contactService.updateContact(1, 10, updatedContact);

        assertEquals("Arisha", result.getFirstName());
        assertEquals("Fatima", result.getLastName());
        assertEquals("Student", result.getTitle());

        verify(contactRepository).save(existingContact);
    }

    @Test
    void exportContactsToCsvSuccessfully() {
        Contact contact = new Contact();
        contact.setFirstName("Ali");
        contact.setLastName("Khan");
        contact.setTitle("Student");

        when(contactRepository.findByUserId(10)).thenReturn(List.of(contact));

        String result = contactService.exportContactsToCsv(10);

        assertEquals(
        "First Name,Last Name,Title\r\nAli,Khan,Student\r\n",
        result
        );

        verify(contactRepository).findByUserId(10);
    }

    @Test
    void importContactsFromCsvSuccessfully() throws Exception { 
        User user = new User();
        user.setId(10);

        String csv = """
            First Name,Last Name,Title
            Ali,Khan,Student
            Sara,Ahmed,Developer
            """;

        int result = contactService.importContactsFromCsv(csv, user);

        assertEquals(2, result);
        verify(contactRepository, times(2)).save(any(Contact.class));
    }

    @Test
    void csvRoundTripPreservesSpecialCharacters() throws Exception {
        User user = new User();
        user.setId(10);

        Contact originalContact = new Contact();
        originalContact.setFirstName("Ali, Jr.");
        originalContact.setLastName("O\"Connor");
        originalContact.setTitle("Senior\nDeveloper");

        when(contactRepository.findByUserId(10)).thenReturn(List.of(originalContact));

        String csv = contactService.exportContactsToCsv(10);

        int importedCount = contactService.importContactsFromCsv(csv, user);

        ArgumentCaptor<Contact> contactCaptor = ArgumentCaptor.forClass(Contact.class);

        verify(contactRepository).save(contactCaptor.capture());

        Contact importedContact = contactCaptor.getValue();

        assertEquals(1, importedCount);
        assertEquals("Ali, Jr.", importedContact.getFirstName());
        assertEquals("O\"Connor", importedContact.getLastName());
        assertEquals("Senior\nDeveloper", importedContact.getTitle());
    }

    @Test
    void malformedCsvDoesNotSaveAnyContacts() {
        User user = new User();
        user.setId(10);

        String csv = "First Name,Last Name,Title\n"
                + "Ali,Khan,Student\n"
                + "\"Sara,Ahmed,Developer";

        assertThrows(IllegalArgumentException.class, () -> contactService.importContactsFromCsv(csv, user));

        verify(contactRepository, times(0)).save(any(Contact.class));
    }
}
