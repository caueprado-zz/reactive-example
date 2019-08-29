package com.reactiveexample.exception;

import lombok.Getter;
import lombok.Setter;

public class CustomException extends RuntimeException {

    @Getter
    @Setter
    private String message;

    public CustomException(Throwable err) {
        super(err);
    }

    public CustomException(String message, Throwable err) {
        super(message, err);
    }
}
