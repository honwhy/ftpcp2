package com.honey;

import org.apache.commons.pool2.impl.GenericObjectPool;

public class BasicFTPClientManager extends FTPClientManagerConfig implements FTPClientManager {

    private volatile FTPClientManager manager;
    private GenericObjectPool<PooledFTPClient> connectionPool;
    private boolean closed;
    @Override
    public PooledFTPClient getFTPClient() throws Exception {
        return createManager().getFTPClient();
    }

    private FTPClientManager createManager() throws Exception {
        if (closed) {
            throw new Exception("manager is closed");
        }
        // Return the pool if we have already created it
        // This is double-checked locking. This is safe since dataSource is
        // volatile and the code is targeted at Java 5 onwards.
        if (manager != null) {
            return manager;
        }
        synchronized (this) {
            if (manager != null) {
                return manager;
            }

            // Set up the poolable connection factory
            PoolableConnectionFactory poolableConnectionFactory = createPoolableConnectionFactory();

            // create a pool for our connections
            createConnectionPool(poolableConnectionFactory);

            // Create the pooling manager to manage connections
            FTPClientManager newManager  = createFTPClientManager();

            // If initialSize > 0, preload the pool
            try {
                for (int i = 0; i < initialSize; i++) {
                    connectionPool.addObject();
                }
            } catch (final Exception e) {
                closeConnectionPool();
                throw new Exception("Error preloading the connection pool", e);
            }

            // If timeBetweenEvictionRunsMillis > 0, start the pool's evictor task
            startPoolMaintenance();

            manager = newManager;
            return manager;
        }
    }

    /**
     * Starts the connection pool maintenance task, if configured.
     */
    protected void startPoolMaintenance() {
        long timeBetweenEvictionRunsMillis = getTimeBetweenEvictionRunsMillis();
        if (connectionPool != null && timeBetweenEvictionRunsMillis > 0) {
            connectionPool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
    }

    private FTPClientManager createFTPClientManager() {
        return new PoolingFTPClientManager(connectionPool);
    }

    private void createConnectionPool(PoolableConnectionFactory poolableConnectionFactory) {
        GenericObjectPool<PooledFTPClient> objectPool = new GenericObjectPool<>(poolableConnectionFactory, this);
        poolableConnectionFactory.connectionPool = objectPool;
        this.connectionPool = objectPool;
    }

    private void closeConnectionPool() {
    }

    private PoolableConnectionFactory createPoolableConnectionFactory() {
        return new PoolableConnectionFactory(this);
    }

    @Override
    public void close() throws Exception {
        if (closed) {
            return ;
        }
        synchronized (this) {
            closed = true;
            try{
                manager.close();
            } catch (Exception e) {
                //swallow exception
            }
        }
    }
}
