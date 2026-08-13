package com.contactmanagement.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.contactmanagement.backend.entity.ContactEmail;
import com.contactmanagement.backend.repository.ContactEmailRepository;

@Service
public class ContactEmailService {
    private final ContactEmailRepository contactEmailRepository;
    private final ContactService contactService;

    public ContactEmailService(ContactEmailRepository contactEmailRepository, ContactService contactService) {
        this.contactEmailRepository = contactEmailRepository;
        this.contactService = contactService;
    }
    
    public List<ContactEmail> getEmailsByContactId(Integer contactId, Integer userId) { // Get all emails belonging to one contact

        if (contactService.getContactById(contactId, userId).isEmpty()) {
            throw new IllegalArgumentException("Contact not found");
        }
        return contactEmailRepository.findByContactId(contactId);
    }

    public Optional<ContactEmail> getContactEmailById(Integer id) {
        return contactEmailRepository.findById(id);     // Find a contact email by ID
    }

    public void deleteContactEmail(Integer id) {
        contactEmailRepository.deleteById(id);    // Delete a contact email by ID
    }
    
}
