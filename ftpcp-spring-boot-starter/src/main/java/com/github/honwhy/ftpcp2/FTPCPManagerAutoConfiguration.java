package com.github.honwhy.ftpcp2;

import com.github.honwhy.BasicFTPClientManager;
import com.github.honwhy.FTPClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@ConditionalOnClass(BasicFTPClientManager.class)
@EnableConfigurationProperties(FTPCPManagerProperties.class)
public class FTPCPManagerAutoConfiguration {


    @Autowired
    private FTPCPManagerProperties ftpcpManagerProperties;
    private FTPClientManager ftpClientManager;

    @Bean
    @ConditionalOnMissingBean
    public FTPClientManager ftpClientManager() {

        BasicFTPClientManager manager = new BasicFTPClientManager();
        FTPCPManagerProperties p = ftpcpManagerProperties;

        /**
         * for ftp connection settings
         */
        manager.setServerTimeZoneId(p.getServerTimeZoneId());
        manager.setBufferSize(p.getBufferSize());
        manager.setControlEncoding(p.getControlEncoding());
        manager.setKeepAliveTimeout(p.getKeepAliveTimeout());
        manager.setControlKeepAliveReplyTimeout(p.getControlKeepAliveReplyTimeout());
        manager.setConnectTimeout(p.getConnectTimeout());
        manager.setDataConnectionMode(p.getDataConnectionMode());
        manager.setHost(p.getHost());
        manager.setPort(p.getPort());
        manager.setUsername(p.getUsername());
        manager.setPassword(p.getPassword());

        /**
         * for connection pool properties
         */
        manager.setInitialSize(p.getInitialSize());
        manager.setMaxIdle(p.getMaxIdle());
        manager.setMaxTotal(p.getMaxTotal());
        manager.setMaxWaitMillis(p.getMaxWaitMillis());
        manager.setTestOnCreate(p.getTestOnCreate());
        manager.setTestOnBorrow(p.getTestOnBorrow());
        manager.setTestOnReturn(p.getTestOnReturn());
        manager.setTestWhileIdle(p.getTestWhileIdle());

        this.ftpClientManager = manager;
        return this.ftpClientManager;
    }

    @PreDestroy
    public void close() {
        if (this.ftpClientManager != null) {
            try {
                this.ftpClientManager.close();
            } catch (Exception e) {
                //swallow exception
            }
        }
    }

}
