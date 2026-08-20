package com.contactmanagement.backend.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanagement.backend.dto.ContactRequest;
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

    //2. fetch contacts by id- CRUD action: Get /api/contacts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Integer id, @AuthenticationPrincipal User user){
        return ResponseEntity.of(contactService.getContactById(id, user.getId()));
    }

    //3. Creating a contact- CRUD action: POST /api/contacts
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody ContactRequest request, @AuthenticationPrincipal User user) {

        Contact contact = new Contact(request.getFirstName(), request.getLastName(), request.getTitle(), user);
        Contact savedContact = contactService.saveContact(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    //4. Delete contact by id- CRUD action: DELETE /api/contacts/{id}
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Integer id, @AuthenticationPrincipal User user) {

    contactService.deleteContact(id, user.getId());
    }

    //5. Update a contact- CRUD action: PUT /api/contacts/{id}
    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Integer id, @RequestBody ContactRequest request, @AuthenticationPrincipal User user) {
        Contact updatedContact = new Contact(request.getFirstName(), request.getLastName(), request.getTitle(), user);
        return contactService.updateContact(id, user.getId(), updatedContact);
    }

    //6. Search from logged-in user's own contact list- CRUD action: GET /api/contacts/search?searchTerm={name}
    @GetMapping("/search")
    public Page<Contact> searchContacts(@RequestParam String searchTerm, Pageable pageable, @AuthenticationPrincipal User user) {

        return contactService.searchContacts(user.getId(), searchTerm, pageable);
    }

    //csv export
    @GetMapping("/export")
    public ResponseEntity<String> exportContacts(@AuthenticationPrincipal User user) {
        String csv = contactService.exportContactsToCsv(user.getId());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contacts.csv")
            .header(HttpHeaders.CONTENT_TYPE, "text/csv")
            .body(csv);
    }

    //csv import
    @PostMapping("/import")
    public ResponseEntity<String> importContacts(@RequestPart("file") MultipartFile file,
        @AuthenticationPrincipal User user) throws IOException {

        String csvContent = new String(file.getBytes());
        int importedCount = contactService.importContactsFromCsv(csvContent, user);

        return ResponseEntity.ok(importedCount + " contacts imported successfully");
    }

}
