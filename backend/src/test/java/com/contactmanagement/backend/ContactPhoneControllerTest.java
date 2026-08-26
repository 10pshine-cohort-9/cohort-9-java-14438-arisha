package com.contactmanagement.backend;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.contactmanagement.backend.controller.ContactPhoneController;
import com.contactmanagement.backend.service.ContactPhoneService;

@ExtendWith(MockitoExtension.class)
public class ContactPhoneControllerTest {
        @Mock
    private ContactPhoneService contactPhoneService;

    @InjectMocks
    private ContactPhoneController contactPhoneController;
    
}
