package com.github.honwhy;

import org.apache.ftpserver.test.ClientTestTemplate;

public abstract class BasicFTPClientTestTemplate extends ClientTestTemplate {

    @Override
    protected boolean isConnectClient() {
        return false;
    }
}
