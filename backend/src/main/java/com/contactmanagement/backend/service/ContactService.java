package com.contactmanagement.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.repository.ContactRepository;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Page<Contact> getAllContacts(Pageable pageable) {
        return contactRepository.findAll(pageable);     // Get contacts one page at a time
    }

    public Optional<Contact> getContactById(Integer id) {
        return contactRepository.findById(id);     // Find a contact by ID
    }

    public void deleteContact(Integer id) {
        contactRepository.deleteById(id); // Delete a contact by ID
    }
    
}
