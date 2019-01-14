package com.honey;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;


public class PooledFTPClient extends FTPClient {

    /**
     * ref to object pool which this object will return to
     */
    protected GenericObjectPool<PooledFTPClient> connectionPool;
    private boolean isLogout;
    private boolean isDisconnected;
    private long createTimestamp;

    public PooledFTPClient(GenericObjectPool<PooledFTPClient> connectionPool) {
        this.createTimestamp = System.currentTimeMillis();
        this.connectionPool = connectionPool;
    }
    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        isDisconnected = true;
    }

    @Override
    public boolean logout() throws IOException {
        boolean ret = super.logout(); //will send out ftp quit command
        isLogout = true;
        return ret;
    }

    public void holdConnection() throws IOException {
        Utils.checkNonNull(connectionPool,"connection pool is null, this SHOULD NOT HAPPEN!!!");
        try {
            if (!isDisconnected && !isLogout) {
                connectionPool.returnObject(this);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    protected void setConnectionPool(GenericObjectPool<PooledFTPClient> connectionPool) {
        this.connectionPool = connectionPool;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }
}
