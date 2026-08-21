package com.contactmanagement.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.contactmanagement.backend.dto.ContactPhoneRequest;
import com.contactmanagement.backend.entity.Contact;
import com.contactmanagement.backend.entity.ContactPhone;
import com.contactmanagement.backend.repository.ContactPhoneRepository;

@Service
public class ContactPhoneService {
    private final ContactPhoneRepository contactPhoneRepository;
    private final ContactService contactService;

    public ContactPhoneService(ContactPhoneRepository contactPhoneRepository, ContactService contactService) {
        this.contactPhoneRepository = contactPhoneRepository;
        this.contactService = contactService;
    }

    public List<ContactPhone> getPhonesByContactId(Integer contactId, Integer userId) {
        if (contactService.getContactById(contactId, userId).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }
        return contactPhoneRepository.findByContactId(contactId);
    }

    //1. Get contact phone by id
    public Optional<ContactPhone> getContactPhoneById(Integer id,Integer userId) {
        Optional<ContactPhone> contactPhone = contactPhoneRepository.findById(id);

        if (contactPhone.isEmpty()){
            return Optional.empty();
        }
        Integer contactId = contactPhone.get().getContact().getId();
        if (contactService.getContactById(contactId, userId).isEmpty()){
            return Optional.empty();
        }

        return contactPhone;
    }

    //2. Create contact phone
    public ContactPhone createContactPhone(Integer contactId, Integer userId, ContactPhoneRequest request) {
        Optional<Contact> contact = contactService.getContactById(contactId, userId);

        if (contact.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }

        String phoneNumber = request.getPhoneNumber();
        String label = request.getLabel();
        Contact foundContact = contact.get();

        ContactPhone contactPhone = new ContactPhone(phoneNumber, label, foundContact);
        ContactPhone savedPhone = contactPhoneRepository.save(contactPhone);
        return savedPhone;
    }

    //3. update contact phone
    public Optional<ContactPhone> updateContactPhone(Integer id, Integer userId, ContactPhoneRequest request) {
        Optional<ContactPhone> contactPhone = getContactPhoneById(id, userId);

        if (contactPhone.isEmpty()) {
            return Optional.empty();
        }
        //update phone number
        ContactPhone foundPhone = contactPhone.get();
        foundPhone.setPhoneNumber(request.getPhoneNumber());

        //update label
        foundPhone.setLabel(request.getLabel());

        ContactPhone savedPhone = contactPhoneRepository.save(foundPhone);
        return Optional.of(savedPhone);
    }

    //4. delete contact phone
    public boolean deleteContactPhone(Integer id, Integer userId){
        Optional<ContactPhone> contactPhone = getContactPhoneById(id, userId);
        if (contactPhone.isEmpty()){
            return false;
        }
        contactPhoneRepository.deleteById(id);
        return true;
    }
}
