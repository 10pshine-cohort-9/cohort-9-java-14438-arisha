package com.contactmanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactmanagement.backend.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

}