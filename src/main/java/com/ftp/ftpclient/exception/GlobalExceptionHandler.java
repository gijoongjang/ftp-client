package com.ftp.ftpclient.exception;

import com.ftp.ftpclient.dto.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {BadReqeustException.class})
    protected ResponseEntity<?> badRequestException(HttpServletRequest request, Exception e) {
        logger.error("ERROR : {}", e.getMessage());
        return ErrorResponseDTO.toResponseEntity(request, e.getMessage());
    }

    @ExceptionHandler(value = {FTPException.class})
    protected ResponseEntity<?> ftpException(HttpServletRequest reqeust, FTPException e) {
        logger.error("FTP ERROR : {}", e.getErrorCode().getMessage());
        return ErrorResponseDTO.toResponseEntity(reqeust, e.getErrorCode());
    }

    @ExceptionHandler(value = {BindException.class})
    protected ResponseEntity<?> bindException(HttpServletRequest reqeust, BindException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder sb = new StringBuilder();
        if (bindingResult.getFieldErrors().size() == 0) {
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                sb.append(objectError.getDefaultMessage());
            }
        } else {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append("[");
                sb.append(fieldError.getField());
                sb.append("]은(는)");
                sb.append(fieldError.getDefaultMessage());
                sb.append(" 입력된 값: [");
                sb.append(fieldError.getRejectedValue());
                sb.append("]");
            }
        }

        return ErrorResponseDTO.toResponseEntity(reqeust, sb.toString());
    }
}
