package com.honey;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;


public class PooledFTPClient extends FTPClient {

    /**
     * ref to object pool which this object will return to
     */
    protected GenericObjectPool<PooledFTPClient> connectionPool;

    public PooledFTPClient(GenericObjectPool<PooledFTPClient> connectionPool) {
        this.connectionPool = connectionPool;
    }
    @Override
    public void disconnect() throws IOException {
        Utils.checkNonNull(connectionPool,"connection pool is null, this SHOULD NOT HAPPEN!!!");
        try {
            connectionPool.returnObject(this);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void superDisconnect() throws IOException {
        super.disconnect();
    }

    protected void setConnectionPool(GenericObjectPool<PooledFTPClient> connectionPool) {
        this.connectionPool = connectionPool;
    }
}
