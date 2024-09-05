package com.aungPyae22.forgot_mail_sender.exceptions;

public class FieldRequireException extends RuntimeException{
    public FieldRequireException(String message) {
        super(message);
    }

    public FieldRequireException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldRequireException(Throwable cause) {
        super(cause);
    }
}
