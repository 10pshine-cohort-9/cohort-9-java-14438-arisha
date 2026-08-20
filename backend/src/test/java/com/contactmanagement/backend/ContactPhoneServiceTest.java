package com.contactmanagement.backend;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.contactmanagement.backend.repository.ContactPhoneRepository;
import com.contactmanagement.backend.service.ContactPhoneService;
import com.contactmanagement.backend.service.ContactService;

@ExtendWith(MockitoExtension.class)
public class ContactPhoneServiceTest {
    @Mock
    private ContactPhoneRepository contactPhoneRepository;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactPhoneService contactPhoneService;
    
}
