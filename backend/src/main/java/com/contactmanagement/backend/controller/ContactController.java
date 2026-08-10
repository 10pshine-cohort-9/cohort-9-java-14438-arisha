package com.contactmanagement.backend.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.entity.User;
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
    public Page<Contact> getAllContacts(Pageable pageable, @AuthenticationPrincipal User user){
        return contactService.getAllContacts(user.getId(), pageable);
    }

    //2. fetch contacts by id- CRUD action: Get /api/contacts/5
    @GetMapping("/{id}")
    public Optional<Contact> getContactById(@PathVariable Integer id, @AuthenticationPrincipal User user){
        return contactService.getContactById(id, user.getId());
    }

    //3. Creating a contact- CRUD action: POST /api/contacts
    @PostMapping
    public Contact createContact(@RequestBody Contact contact, @AuthenticationPrincipal User user){
        contact.setUser(user);
        return contactService.saveContact(contact);
    }

    //4. Delete contact by id- CRUD action: DELETE /api/contacts/5
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Integer id, @AuthenticationPrincipal User user) {

    contactService.deleteContact(id, user.getId());
}

}
