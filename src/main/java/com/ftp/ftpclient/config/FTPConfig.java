package com.ftp.ftpclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "ftp")
public class FTPConfig {
    private String host;
    private String id;
    private String pw;
}
