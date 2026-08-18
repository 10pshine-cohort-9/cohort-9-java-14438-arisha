package com.contactmanagement.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.contactmanagement.backend.dto.ContactEmailRequest;
import com.contactmanagement.backend.entity.Contact;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
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

    public ContactEmail createContactEmail(Integer contactId, Integer userId, ContactEmailRequest request) {
        Optional<Contact> contact = contactService.getContactById(contactId, userId);

        if (contact.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }

        String emailAddress = request.getEmailAddress();
        String label = request.getLabel();
        Contact foundContact = contact.get();
        ContactEmail contactEmail = new ContactEmail(emailAddress, label, foundContact);

        ContactEmail savedEmail = contactEmailRepository.save(contactEmail);
        return savedEmail;
    }

    public Optional<ContactEmail> updateContactEmail(Integer id, Integer userId, ContactEmailRequest request) {
        Optional<ContactEmail> contactEmail = getContactEmailById(id, userId);

        if (contactEmail.isEmpty()) {
            return Optional.empty();
        }

        ContactEmail foundEmail = contactEmail.get();
        foundEmail.setEmailAddress(request.getEmailAddress());
        foundEmail.setLabel(request.getLabel());

        ContactEmail savedEmail = contactEmailRepository.save(foundEmail);
        return Optional.of(savedEmail);
    }
    
}
