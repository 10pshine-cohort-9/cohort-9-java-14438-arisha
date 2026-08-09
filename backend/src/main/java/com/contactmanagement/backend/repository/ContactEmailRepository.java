package com.contactmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactmanagement.backend.entity.ContactEmail;

public interface ContactEmailRepository extends JpaRepository<ContactEmail, Integer> {

    List<ContactEmail> findByContactId(Integer contactId); // Find all email addresses belonging to one contact
}
