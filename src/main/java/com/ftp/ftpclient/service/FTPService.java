package com.ftp.ftpclient.service;

import com.ftp.ftpclient.exception.FTPException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public interface FTPService {
    void connectToFTP() throws FTPException;
    boolean uploadFile(String filePath, String fileName, MultipartFile file) throws FTPException;
    void downloadFile(String filePath, OutputStream os) throws FTPException;
    boolean deleteFile(String filePath) throws FTPException;
}
