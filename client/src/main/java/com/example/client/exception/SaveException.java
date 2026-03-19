package com.example.client.exception;

import java.io.Serial;

public class SaveException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public SaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
