package com.ftp.ftpclient.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FTPException extends RuntimeException {
    private final ErrorCode errorCode;
}
