package com.contactmanagement.backend.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        StringWriter writer = new StringWriter();

        try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            printer.printRecord("First Name", "Last Name", "Title");

            for (Contact contact : contacts) {
                String title = contact.getTitle() == null
                    ? ""
                    : contact.getTitle();

                printer.printRecord(contact.getFirstName(), contact.getLastName(), title);
            }

        } catch (IOException exception) {
            throw new IllegalStateException(
                "Unable to export contacts",
                exception
             );
        }

        logger.info(
            "Exported {} contacts for user ID: {}",
            contacts.size(),
            userId
        );

        return writer.toString();
    }

    @Transactional
    public int importContactsFromCsv(String csvContent, User user) {

        List<Contact> contactsToImport = new ArrayList<>();

        CSVFormat format = CSVFormat.DEFAULT.builder().setHeader("First Name", "Last Name", "Title").setSkipHeaderRecord(true).get();

        try (CSVParser parser = CSVParser.parse(csvContent, format)) {

            for (CSVRecord record : parser) {

                if (record.size() < 2) {
                    throw new IllegalArgumentException("Invalid CSV file");
                }

                String firstName = record.get(0).trim();
                String lastName = record.get(1).trim();

                String title = "";
                if (record.size() > 2) {
                    title = record.get(2).trim();
                }

                Contact contact = new Contact(firstName, lastName, title, user);

                contactsToImport.add(contact);
            }

        } catch (IOException | IllegalStateException | UncheckedIOException exception) {
            throw new IllegalArgumentException("Invalid CSV file", exception);
        }

        for (Contact contact : contactsToImport) {
            contactRepository.save(contact);
        }

        logger.info("Imported {} contacts for user ID: {}", contactsToImport.size(), user.getId());

        return contactsToImport.size();
    }
}
