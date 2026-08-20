package com.contactmanagement.backend.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.repository.ContactRepository;

@Service
public class ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact saveContact(Contact contact) {
            Contact savedContact = contactRepository.save(contact);
            logger.info("Contact created with ID: {} for user ID: {}", savedContact.getId(), savedContact.getUser().getId());
            return savedContact;
        }

    public Page<Contact> getAllContacts(Integer userId, Pageable pageable) {
        return contactRepository.findByUserId(userId, pageable);     // Get contacts one page at a time
    }

    public Optional<Contact> getContactById(Integer id, Integer userId) {
        return contactRepository.findByIdAndUserId(id, userId);    // Find a contact by ID
    }

    public void deleteContact(Integer id, Integer userId) {
        Optional<Contact> contact = contactRepository.findByIdAndUserId(id, userId);

        if (contact.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }
        contactRepository.delete(contact.get());
        logger.info("Contact deleted with ID: {} for user ID: {}", id, userId);
    }
    
    public Contact updateContact(Integer id, Integer userId, Contact updatedContact) {

        Optional<Contact> contact = contactRepository.findByIdAndUserId(id, userId);

        if (contact.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }
        Contact existingContact = contact.get();

        existingContact.setFirstName(updatedContact.getFirstName());
        existingContact.setLastName(updatedContact.getLastName());
        existingContact.setTitle(updatedContact.getTitle());

        Contact savedContact = contactRepository.save(existingContact);
        logger.info("Contact updated with ID: {} for user ID: {}", id, userId);
        return savedContact;
    }

    public Page<Contact> searchContacts(Integer userId, String searchTerm, Pageable pageable) {

        return contactRepository.findByUserIdAndFirstNameContainingIgnoreCaseOrUserIdAndLastNameContainingIgnoreCase(
            userId, searchTerm, userId, searchTerm, pageable);
    }

    public String exportContactsToCsv(Integer userId) {

        List<Contact> contacts = contactRepository.findByUserId(userId);
        StringBuilder csv = new StringBuilder();

        csv.append("First Name,Last Name,Title\n");

        for (Contact contact : contacts) {
            csv.append(contact.getFirstName()).append(",");
            csv.append(contact.getLastName()).append(",");
            csv.append(contact.getTitle() == null ? "" : contact.getTitle()).append("\n");
        }

        logger.info("Exported {} contacts for user ID: {}", contacts.size(), userId); 
        return csv.toString();
    }

    public int importContactsFromCsv(String csvContent, User user) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));

        String line;
        int importedCount = 0;

        reader.readLine(); // skip CSV header

        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");

            if (values.length < 2) {
                continue;
            }

            String firstName = values[0].trim();
            String lastName = values[1].trim();
            String title = values.length > 2 ? values[2].trim() : "";

            Contact contact = new Contact(firstName, lastName, title, user);
            contactRepository.save(contact);
            importedCount++;
        }

        logger.info("Imported {} contacts for user ID: {}", importedCount, user.getId());

        return importedCount;
    }
}
