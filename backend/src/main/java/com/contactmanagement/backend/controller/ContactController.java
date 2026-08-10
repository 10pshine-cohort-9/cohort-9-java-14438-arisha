package com.contactmanagement.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.contactmanagement.backend.entity.Contact;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Page<Contact> getAllContacts(Pageable pageable){
        return contactService.getAllContacts(pageable);
    }

}
