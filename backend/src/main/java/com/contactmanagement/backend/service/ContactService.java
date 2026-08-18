package com.contactmanagement.backend.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.contactmanagement.backend.entity.Contact;
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
}
