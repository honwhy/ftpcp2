package com.github.honwhy;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class FTPClientManagerConfig extends GenericObjectPoolConfig {

    protected int initialSize;

    protected String serverTimeZoneId;
    protected Integer bufferSize;
    protected String controlEncoding;
    protected Long keepAliveTimeout;
    protected Integer controlKeepAliveReplyTimeout;
    protected Integer connectTimeout;
    protected String dataConnectionMode;


    protected String host;
    protected int port;

    protected String username;
    protected String password;

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public String getServerTimeZoneId() {
        return serverTimeZoneId;
    }

    public void setServerTimeZoneId(String serverTimeZoneId) {
        this.serverTimeZoneId = serverTimeZoneId;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getControlEncoding() {
        return controlEncoding;
    }

    public void setControlEncoding(String controlEncoding) {
        this.controlEncoding = controlEncoding;
    }

    public Long getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public void setKeepAliveTimeout(Long keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public Integer getControlKeepAliveReplyTimeout() {
        return controlKeepAliveReplyTimeout;
    }

    public void setControlKeepAliveReplyTimeout(Integer controlKeepAliveReplyTimeout) {
        this.controlKeepAliveReplyTimeout = controlKeepAliveReplyTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getDataConnectionMode() {
        return dataConnectionMode;
    }

    public void setDataConnectionMode(String dataConnectionMode) {
        this.dataConnectionMode = dataConnectionMode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
