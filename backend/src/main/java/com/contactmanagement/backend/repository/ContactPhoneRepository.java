package com.contactmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactmanagement.backend.entity.ContactPhone;

public interface ContactPhoneRepository extends JpaRepository<ContactPhone, Integer> {

    // Find all phone numbers belonging to one contact
    List<ContactPhone> findByContactId(Integer contactId);
}