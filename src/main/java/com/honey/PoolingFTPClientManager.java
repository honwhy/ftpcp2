package com.honey;

import org.apache.commons.pool2.ObjectPool;

public class PoolingFTPClientManager implements FTPClientManager, AutoCloseable {

    private final ObjectPool<PooledFTPClient> objectPool;

    public PoolingFTPClientManager(ObjectPool<PooledFTPClient> objectPool) {
        this.objectPool = objectPool;
    }

    @Override
    public PooledFTPClient getFTPClient() throws Exception {
        return objectPool.borrowObject();
    }

    @Override
    public void close() throws Exception {
        objectPool.close();
    }
}
