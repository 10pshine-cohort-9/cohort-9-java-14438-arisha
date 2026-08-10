package com.contactmanagement.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.contactmanagement.backend.entity.ContactEmail;
import com.contactmanagement.backend.repository.ContactEmailRepository;

@Service
public class ContactEmailService {
    private final ContactEmailRepository contactEmailRepository;

    public ContactEmailService(ContactEmailRepository contactEmailRepository) {
        this.contactEmailRepository = contactEmailRepository;
    }
    public List<ContactEmail> getEmailsByContactId(Integer contactId) { // Get all emails belonging to one contact
        return contactEmailRepository.findByContactId(contactId);
    }

    public Optional<ContactEmail> getContactEmailById(Integer id) {
        return contactEmailRepository.findById(id);     // Find a contact email by ID
    }

    public void deleteContactEmail(Integer id) {
        contactEmailRepository.deleteById(id);    // Delete a contact email by ID
    }
    
}
