package com.github.honwhy;

public interface BasicFTPClientManagerMBean {
    int getInitialSize();
    int getNumIdle();
    int getNumActive();
    String getHost();
    int getPort();
    String getUsername();

    // get associate connection pool jmx name
    String getConnectionPoolName();
}
