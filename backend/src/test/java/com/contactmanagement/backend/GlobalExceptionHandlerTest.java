package com.contactmanagement.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.contactmanagement.backend.exception.GlobalExceptionHandler;

class GlobalExceptionHandlerTest {

    @Test
    void illegalArgumentExceptionReturnsBadRequest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<String> response =
                handler.handleIllegalArgumentException(
                        new IllegalArgumentException("Invalid data")
                );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request", response.getBody());
    }
}
