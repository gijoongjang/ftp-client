package com.ftp.ftpclient.service;

import com.ftp.ftpclient.config.FTPConfig;
import com.ftp.ftpclient.exception.FTPException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import static com.ftp.ftpclient.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class FTPServiceImpl implements FTPService {
    private final Logger logger = LoggerFactory.getLogger(FTPServiceImpl.class);
    private final FTPConfig ftpConfig;
    private FTPClient ftpClient;

    @Override
    public void connectToFTP() throws FTPException {
        ftpClient = new FTPClient();

        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try {
            ftpClient.setControlEncoding("EUC-KR");
            ftpClient.connect(ftpConfig.getHost());

            int replyCode = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(replyCode)) {
                logger.error("ftp connect failed : {}", ftpConfig.getHost());
                ftpClient.disconnect();
                throw new FTPException(FTP_NOT_CONNECT);
            }
        } catch (IOException e) {
            logger.error("ftp connect failed : {}", e.getMessage());
            throw new FTPException(FTP_NOT_CONNECT);
        }

        try {
            ftpClient.login(ftpConfig.getId(), ftpConfig.getPw());
        } catch (IOException e) {
            logger.error("ftp connect failed : {}", e.getMessage());
            throw new FTPException(FTP_USER_NOT_FOUND);
        }
    }

    @Override
    public boolean uploadFile(String filePath, String fileName, MultipartFile file) {
        boolean uploadedSuccess = false;
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            toPathOrCreateDir(filePath);

            InputStream is = file.getInputStream();

            uploadedSuccess = ftpClient.storeFile(fileName, is);

            is.close();
        } catch (Exception e) {
            logger.error("ftp file upload failed : {}", e.getMessage());
        } finally {
            disconnectFTP();
        }

        return uploadedSuccess;
    }

    @Override
    public void downloadFile(String filePath, OutputStream os) {
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(filePath, os);
        } catch (Exception e) {
            logger.error("file download error : {}", e.getMessage());
            throw new FTPException(FTP_FILE_DOWNLOAD_ERROR);
        } finally {
            disconnectFTP();
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        boolean deletedSuccess = false;
        try {
            deletedSuccess = ftpClient.deleteFile(filePath);
        } catch (Exception e) {
            logger.error("ftp file delete failed {}", e.getMessage());
        } finally {
            disconnectFTP();
        }

        return deletedSuccess;
    }

    private void toPathOrCreateDir(String filePath) throws IOException {
        String[] dirs = filePath.split("/");

        if(dirs == null && dirs.length < 1) return;
        for(String dir : dirs) {
            if(StringUtils.isEmpty(dir)) continue;
            if(!ftpClient.changeWorkingDirectory(dir)) {
                ftpClient.makeDirectory(dir);
                ftpClient.changeWorkingDirectory(dir);
            }
        }
    }

    private void disconnectFTP() throws FTPException {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                throw new FTPException(INTERNAL_SERVER_ERROR);
            }
        }
    }
}