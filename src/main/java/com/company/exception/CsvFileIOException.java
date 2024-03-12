package com.company.exception;

import java.io.IOException;

public class CsvFileIOException extends RuntimeException {

    public CsvFileIOException(IOException exception) {
        super(exception);
    }
}
