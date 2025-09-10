package org.example.backend2.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String message, DataIntegrityViolationException e) {
        super(message);
    }
}
