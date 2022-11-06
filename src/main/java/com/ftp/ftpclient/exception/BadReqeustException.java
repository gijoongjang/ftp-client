package com.ftp.ftpclient.exception;

public class BadReqeustException extends RuntimeException {
    public BadReqeustException() {
        super();
    }

    public BadReqeustException(String message) {
        super(message);
    }
}
