package com.ftp.ftpclient.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class FTPFileDownloadRequestDTO {
    @NotEmpty
    private String filePath;
    @NotEmpty
    private String fileName;
}
