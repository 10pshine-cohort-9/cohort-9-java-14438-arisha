package com.contactmanagement.backend.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.service.ContactService;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    //1. fetch all contacts- CRUD action: GET /api/contacts
    @GetMapping
    public Page<Contact> getAllContacts(Pageable pageable){
        return contactService.getAllContacts(pageable);
    }

    //2. fetch contacts by id- CRUD action: Get /api/contact/5
    @GetMapping("/{id}")
    public Optional<Contact> getContactById(@PathVariable Integer id){
        return contactService.getContactById(id);
    }

}
