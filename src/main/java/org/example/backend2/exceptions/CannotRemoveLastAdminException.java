package org.example.backend2.exceptions;

public class CannotRemoveLastAdminException extends RuntimeException {
    public CannotRemoveLastAdminException(String msg) { super(msg); }
}
