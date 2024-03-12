package com.company.exception;

public class InvalidCsvFileFormatException extends RuntimeException {

    public InvalidCsvFileFormatException(String message) {
        super(message);
    }
}
