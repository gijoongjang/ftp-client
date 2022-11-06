package com.ftp.ftpclient.dto;

import com.ftp.ftpclient.exception.ErrorCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDTO {
    private final int code;
    private final boolean success = false;
    private final String message;
    private final String method;
    private final String uri;
    private final LocalDateTime time = LocalDateTime.now();

    public static ResponseEntity<ErrorResponseDTO> toResponseEntity(HttpServletRequest request, ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponseDTO.builder()
                        .code(errorCode.getStatus().value())
                        .message(errorCode.getMessage())
                        .method(request.getMethod())
                        .uri(request.getRequestURI())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponseDTO> toResponseEntity(HttpServletRequest request, String message) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(message)
                        .method(request.getMethod())
                        .uri(request.getRequestURI())
                        .build()
                );
    }
}
