package com.guisebastiao.springbootauth.exceptions;

public class RequiredAuthenticationException extends RuntimeException {
    public RequiredAuthenticationException(String message) {
        super(message);
    }
}
