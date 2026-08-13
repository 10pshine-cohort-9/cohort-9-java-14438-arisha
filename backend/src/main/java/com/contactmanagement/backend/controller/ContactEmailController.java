package com.contactmanagement.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.entity.ContactEmail;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.service.ContactEmailService;

@RestController
@RequestMapping("/api/contact-emails")
public class ContactEmailController {
    private final ContactEmailService contactEmailService;

    public ContactEmailController(ContactEmailService contactEmailService) {
        this.contactEmailService = contactEmailService;
    }

    //1. To get all emails belonging to a single contact - GET /api/contact-emails/contact/5
    @GetMapping("/contact/{contactId}")
    public List<ContactEmail> getEmailsByContactId(@PathVariable Integer contactId, @AuthenticationPrincipal User user) {
        return contactEmailService.getEmailsByContactId(contactId, user.getId());
    }

    //2. To get one contact email by its id - GET /api/contact-emails/3
    @GetMapping("/{id}")
    public ResponseEntity<ContactEmail> getContactEmailById(@PathVariable Integer id, @AuthenticationPrincipal User user) {
            Optional<ContactEmail> contactEmail = contactEmailService.getContactEmailById(id, user.getId());

            if (contactEmail.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(contactEmail.get());
    }

    //3. Delete emails by id - DELETE /api/contact-emails/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactEmail(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        boolean deleted = contactEmailService.deleteContactEmail(id, user.getId());

        return ResponseEntity.noContent().build();
    }
    
}
