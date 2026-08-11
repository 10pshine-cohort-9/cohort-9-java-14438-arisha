package com.contactmanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactmanagement.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
