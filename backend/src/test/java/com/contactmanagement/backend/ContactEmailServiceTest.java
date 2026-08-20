package com.contactmanagement.backend;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.contactmanagement.backend.repository.ContactEmailRepository;
import com.contactmanagement.backend.service.ContactEmailService;
import com.contactmanagement.backend.service.ContactService;

@ExtendWith(MockitoExtension.class)
public class ContactEmailServiceTest {
    @Mock
    private ContactEmailRepository contactEmailRepository;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactEmailService contactEmailService;
    
}
