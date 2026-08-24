package com.contactmanagement.backend.dto;

public class ContactPhoneRequest {
    private String phoneNumber;
    private String label;

    public ContactPhoneRequest() {
        // Required by Jackson for JSON deserialization
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
}
