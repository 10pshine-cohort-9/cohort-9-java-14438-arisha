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

    public Optional<ContactEmail> getContactEmailById(Integer id, Integer userId) {
        Optional<ContactEmail> contactEmail = contactEmailRepository.findById(id);

        if (contactEmail.isEmpty()) { return Optional.empty();}    

        Integer contactId = contactEmail.get().getContact().getId();

        if (contactService.getContactById(contactId, userId).isEmpty()) {
            return Optional.empty();
        }
        return contactEmail;
    }

    public boolean deleteContactEmail(Integer id, Integer userId) {
        Optional<ContactEmail> contactEmail = getContactEmailById(id, userId);

        if (contactEmail.isEmpty()) {
            return false;
        }

        contactEmailRepository.deleteById(id);
        return true;
    }
    
}
