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

import com.contactmanagement.backend.dto.ContactPhoneRequest;
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

    @GetMapping("/contact/{contactId}")
    public List<ContactPhone> getPhonesByContactId(@PathVariable Integer contactId, @AuthenticationPrincipal User user) {
        return contactPhoneService.getPhonesByContactId(contactId, user.getId());
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ContactPhone> getPhoneById(@PathVariable Integer id, @AuthenticationPrincipal User user){
        Optional<ContactPhone> contactPhone = contactPhoneService.getContactPhoneById(id, user.getId());

        if (contactPhone.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contactPhone.get());
    }

    @PostMapping("/contact/{contactId}")
    public ResponseEntity<ContactPhone> createContactPhone(@PathVariable Integer contactId, @RequestBody ContactPhoneRequest request,
    @AuthenticationPrincipal User user) {

        ContactPhone createdPhone = contactPhoneService.createContactPhone(contactId, user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPhone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactPhone> updateContactPhone(@PathVariable Integer id, @RequestBody ContactPhoneRequest request,
    @AuthenticationPrincipal User user) {
        Optional<ContactPhone> updatedPhone = contactPhoneService.updateContactPhone(id, user.getId(), request);

        if (updatedPhone.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPhone.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactPhone(@PathVariable Integer id, @AuthenticationPrincipal User user){
        boolean deleted = contactPhoneService.deleteContactPhone(id, user.getId());

        if(!deleted){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    } 
}
