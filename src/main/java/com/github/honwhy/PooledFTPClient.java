package com.github.honwhy;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;


public class PooledFTPClient extends FTPClient implements AutoCloseable {

    /**
     * ref to object pool which this object will return to
     */
    protected GenericObjectPool<PooledFTPClient> connectionPool;
    private boolean quit;
    private boolean disconnected;
    private long createTimestamp;

    public PooledFTPClient(GenericObjectPool<PooledFTPClient> connectionPool) {
        this.createTimestamp = System.currentTimeMillis();
        this.connectionPool = connectionPool;
    }
    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        disconnected = true;
    }

    @Override
    public boolean logout() throws IOException {
        boolean ret = super.logout(); //will send out ftp quit command
        quit = true;
        return ret;
    }

    @Override
    public void close() throws Exception {
        Utils.checkNonNull(connectionPool,"connection pool is null, this SHOULD NOT HAPPEN!!!");
        connectionPool.returnObject(this);
    }

    protected void setConnectionPool(GenericObjectPool<PooledFTPClient> connectionPool) {
        this.connectionPool = connectionPool;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public boolean isQuit() {
        return quit;
    }

    public boolean isDisconnected() {
        return disconnected;
    }
}
