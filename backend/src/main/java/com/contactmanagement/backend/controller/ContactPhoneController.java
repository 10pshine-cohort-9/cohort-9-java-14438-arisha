package com.contactmanagement.backend.controller;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.entity.ContactPhone;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.service.ContactPhoneService;

@RestController
@RequestMapping("/api/contact-phones")
public class ContactPhoneController {
    private final ContactPhoneService contactPhoneService;

    public ContactPhoneController(ContactPhoneService contactPhoneService) {
        this.contactPhoneService = contactPhoneService;
    }

    //1. Get phone by id - GET /api/contact-phones/{id}
    @GetMapping ("/{id}")
    public Optional<ContactPhone> getPhoneById(@PathVariable Integer id, @AuthenticationPrincipal User user){
        return contactPhoneService.getContactPhoneById(id, user.getId());
    }
    
}
