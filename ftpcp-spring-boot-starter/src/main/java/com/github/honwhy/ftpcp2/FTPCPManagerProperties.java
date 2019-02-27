package com.github.honwhy.ftpcp2;

import com.github.honwhy.FTPClientManagerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ftpcp")
public class FTPCPManagerProperties extends FTPClientManagerConfig {

}
