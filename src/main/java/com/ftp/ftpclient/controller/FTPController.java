package com.ftp.ftpclient.controller;

import com.ftp.ftpclient.dto.FTPFileDeleteRequestDTO;
import com.ftp.ftpclient.dto.FTPFileUploadRequestDTO;
import com.ftp.ftpclient.dto.FTPFileDownloadRequestDTO;
import com.ftp.ftpclient.dto.ResponseDTO;
import com.ftp.ftpclient.exception.BadReqeustException;
import com.ftp.ftpclient.exception.FTPException;
import com.ftp.ftpclient.service.FTPService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.ftp.ftpclient.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("FTP")
public class FTPController {
    private final FTPService ftpService;
    private final Logger logger = LoggerFactory.getLogger(FTPController.class);

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestPart(value = "dto") FTPFileUploadRequestDTO dto,
                                               @RequestPart(value = "files", required = false) MultipartFile[] files,
                                               HttpServletRequest request) {

        if(files == null || files.length == 0) {
            throw new BadReqeustException("파일을 선택해주세요.");
        }

        ftpService.connectToFTP();
        try {
            for(MultipartFile file : files) {
                boolean isFileUploaded = ftpService.uploadFile(dto.getFilePath(), dto.getFileName(), file);
                if(!isFileUploaded) {
                    logger.error("{} 업로드 실패", file.getOriginalFilename());
                    throw new FTPException(FTP_FILE_UPLOAD_ERROR);
                }
            }

            ResponseDTO response = ResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .success(true)
                    .method(request.getMethod())
                    .uri(request.getRequestURI())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("파일 업로드 오류 발생 {}", e.getMessage());
            throw new FTPException(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/downloadFile")
    public void downloadFile(@RequestHeader("User-Agent") String agent, @Valid @RequestBody FTPFileDownloadRequestDTO dto, HttpServletResponse response) throws Exception {
        String filename = dto.getFileName();

        if(agent.contains("Trident")) {     //IE
            filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "");
        } else if(agent.contains("Edge")) { //Edge
            filename = URLEncoder.encode(filename, "UTF-8");
        } else {                            //Chrome
            filename = new String(filename.getBytes(StandardCharsets.UTF_8), "ISO-8859-1");
        }

        String path = dto.getFilePath() + dto.getFileName();
        try {
            ftpService.connectToFTP();
            OutputStream os = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setContentType("application/octet-stream");
            ftpService.downloadFile(path, os);
            os.flush();
        } catch (Exception e) {
            throw new FTPException(FTP_FILE_DOWNLOAD_ERROR);
        }
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<?> deleteFile(@Valid @RequestBody FTPFileDeleteRequestDTO dto, HttpServletRequest request) {
        ftpService.connectToFTP();
        boolean isFileDeleted = ftpService.deleteFile(dto.getFilePath());
        if(!isFileDeleted) {
            throw new FTPException(FTP_FILE_DELETE_ERROR);
        }

        ResponseDTO response = ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .method(request.getMethod())
                .uri(request.getRequestURI())
                .build();

        return ResponseEntity.ok(response);
    }
}
