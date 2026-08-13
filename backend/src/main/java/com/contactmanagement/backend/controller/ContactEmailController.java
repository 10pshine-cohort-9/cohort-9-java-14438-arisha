package com.contactmanagement.backend.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    
}
