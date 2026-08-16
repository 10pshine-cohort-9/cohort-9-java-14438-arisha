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
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/contact-phones")
public class ContactPhoneController {
    private final ContactPhoneService contactPhoneService;

    public ContactPhoneController(ContactPhoneService contactPhoneService) {
        this.contactPhoneService = contactPhoneService;
    }

    //1. Get phone by id - GET /api/contact-phones/{id}
    @GetMapping ("/{id}")
    public ResponseEntity<ContactPhone> getPhoneById(@PathVariable Integer id, @AuthenticationPrincipal User user){
        Optional<ContactPhone> contactPhone = contactPhoneService.getContactPhoneById(id, user.getId());

        if (contactPhone.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contactPhone.get());
    }
    
}
