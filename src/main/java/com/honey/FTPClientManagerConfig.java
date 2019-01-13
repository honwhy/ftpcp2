package com.honey;

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

    protected String userName;
    protected String password;
}
