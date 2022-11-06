package com.ftp.ftpclient.dto;

import lombok.Getter;

@Getter
public class FTPFileUploadRequestDTO {
    private String filePath;
    private String fileName;
}
