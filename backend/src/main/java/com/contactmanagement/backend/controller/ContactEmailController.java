package com.contactmanagement.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.dto.ContactEmailRequest;
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

    @GetMapping("/contact/{contactId}")
    public List<ContactEmail> getEmailsByContactId(@PathVariable Integer contactId, @AuthenticationPrincipal User user) {
        return contactEmailService.getEmailsByContactId(contactId, user.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactEmail> getContactEmailById(@PathVariable Integer id, @AuthenticationPrincipal User user) {
            Optional<ContactEmail> contactEmail = contactEmailService.getContactEmailById(id, user.getId());

            if (contactEmail.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(contactEmail.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactEmail(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        boolean deleted = contactEmailService.deleteContactEmail(id, user.getId());
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/contact/{contactId}")
    public ResponseEntity<ContactEmail> createContactEmail(@PathVariable Integer contactId, @RequestBody ContactEmailRequest request, @AuthenticationPrincipal User user) {

        ContactEmail createdEmail = contactEmailService.createContactEmail(contactId, user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactEmail> updateContactEmail(@PathVariable Integer id, @RequestBody ContactEmailRequest request,
    @AuthenticationPrincipal User user) {
        Optional<ContactEmail> updatedEmail = contactEmailService.updateContactEmail(id, user.getId(), request);

        if (updatedEmail.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEmail.get());
    }
    
}
