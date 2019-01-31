package com.github.honwhy;

import org.apache.commons.pool2.ObjectPool;

public class PoolingFTPClientManager implements FTPClientManager {

    private final ObjectPool<PooledFTPClient> objectPool;

    public PoolingFTPClientManager(ObjectPool<PooledFTPClient> objectPool) {
        this.objectPool = objectPool;
    }

    @Override
    public PooledFTPClient getFTPClient() throws Exception {
        return objectPool.borrowObject();
    }

    @Override
    public void close() {
        objectPool.close();
    }
}
