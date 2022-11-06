package com.ftp.ftpclient.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class FTPFileDeleteRequestDTO {
    @NotEmpty
    private String filePath;
}
