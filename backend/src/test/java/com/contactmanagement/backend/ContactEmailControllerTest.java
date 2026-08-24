package com.contactmanagement.backend;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.contactmanagement.backend.controller.ContactEmailController;
import com.contactmanagement.backend.service.ContactEmailService;

@ExtendWith(MockitoExtension.class)
public class ContactEmailControllerTest {
    @Mock
    private ContactEmailService contactEmailService;

    @InjectMocks
    private ContactEmailController contactEmailController;
}
