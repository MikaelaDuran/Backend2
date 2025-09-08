package org.example.backend2.exeptions;

public class CannotRemoveLastAdminException extends RuntimeException {
    public CannotRemoveLastAdminException(String msg) { super(msg); }
}
