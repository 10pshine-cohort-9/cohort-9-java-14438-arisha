package com.contactmanagement.backend.service;

import org.springframework.stereotype.Service;

import com.contactmanagement.backend.repository.ContactPhoneRepository;

@Service
public class ContactPhoneService {
    private final ContactPhoneRepository contactPhoneRepository;
    private final ContactService contactService;

    public ContactPhoneService(ContactPhoneRepository contactPhoneRepository, ContactService contactService) {
        this.contactPhoneRepository = contactPhoneRepository;
        this.contactService = contactService;
    }
    
}
