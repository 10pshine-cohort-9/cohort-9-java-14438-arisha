package com.contactmanagement.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.contactmanagement.backend.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    Page<Contact> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName,
        String lastName,
        Pageable pageable
);

Page<Contact> findByUserId(Integer userId, Pageable pageable);
Optional<Contact> findByIdAndUserId(Integer id, Integer userId);

}