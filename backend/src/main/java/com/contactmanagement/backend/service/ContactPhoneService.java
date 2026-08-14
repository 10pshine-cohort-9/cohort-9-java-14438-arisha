package com.contactmanagement.backend.service;

import org.springframework.stereotype.Service;

import com.contactmanagement.backend.repository.ContactPhoneRepository;
import java.util.List;
import com.contactmanagement.backend.entity.ContactPhone;
import java.util.Optional;


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
            throw new IllegalArgumentException("Contact not found");
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
}
