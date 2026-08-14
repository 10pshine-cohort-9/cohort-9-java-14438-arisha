package com.contactmanagement.backend.dto;

public class ContactEmailRequest {
    private String emailAddress;
    private String label;

    public ContactEmailRequest() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
}
