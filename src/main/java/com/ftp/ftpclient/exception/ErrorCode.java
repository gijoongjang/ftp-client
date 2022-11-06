package com.ftp.ftpclient.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    FTP_NOT_CONNECT(HttpStatus.INTERNAL_SERVER_ERROR, "FTP 접속에 실패하였습니다."),
    FTP_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "FTP 계정 정보를 확인해주세요."),
    FTP_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FTP 파일 찾을 수 없음."),
    FTP_FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패."),
    FTP_FILE_DOWNLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드 실패."),
    FTP_FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 실패."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류.");

    private final HttpStatus status;
    private final String message;
}
