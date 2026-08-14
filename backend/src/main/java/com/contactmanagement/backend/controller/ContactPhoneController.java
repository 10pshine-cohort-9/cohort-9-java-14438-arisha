package com.contactmanagement.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.service.ContactPhoneService;

@RestController
@RequestMapping("/api/contact-phones")
public class ContactPhoneController {
    private final ContactPhoneService contactPhoneService;

    public ContactPhoneController(ContactPhoneService contactPhoneService) {
        this.contactPhoneService = contactPhoneService;
    }
    
}
