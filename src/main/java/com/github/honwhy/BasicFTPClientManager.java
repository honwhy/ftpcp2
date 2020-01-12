package com.github.honwhy;

import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicFTPClientManager extends FTPClientManagerConfig implements BasicFTPClientManagerMBean, FTPClientManager {

    private volatile FTPClientManager manager;
    private GenericObjectPool<PooledFTPClient> connectionPool;
    private ObjectName oname;
    private volatile boolean closed;
    private static AtomicInteger MANAGER_ID = new AtomicInteger(0);
    private String id;

    // JMX specific attributes
    private static final String ONAME_BASE =
            "com.github.honwhy:type=BasicFTPClientManager,name=ftpcp";

    public BasicFTPClientManager() {
        super();
        this.id = String.valueOf(MANAGER_ID.getAndIncrement());
    }
    @Override
    public PooledFTPClient getFTPClient() throws Exception {
        return createManager().getFTPClient();
    }

    private FTPClientManager createManager() throws Exception {
        if (closed) {
            throw new Exception("manager is closed");
        }
        // Return the pool if we have already created it
        // This is double-checked locking. This is safe since manager is
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

            // for jmx
            jmxRegister();

            // If initialSize > 0, preload the pool
            try {
                for (int i = 0; i < initialSize; i++) {
                    connectionPool.addObject();
                }
            } catch (final Exception e) {
                closeConnectionPool();
                throw new Exception("Error preloading the connection pool", e);
            }

            manager = newManager;
            return manager;
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
        if (connectionPool != null) {
            connectionPool.close();
            connectionPool = null; //help gc
        }
    }

    private PoolableConnectionFactory createPoolableConnectionFactory() {
        return new PoolableConnectionFactory(this);
    }

    @Override
    public void close() {
        if (closed) {
            return ;
        }
        synchronized (this) {
            if (closed) {
                return;
            }
            closed = true;
            try{
                jmxUnregister();
                manager.close();
                manager = null;
                connectionPool = null; //help gc
            } catch (Exception e) {
                //swallow exception
            }
        }
    }

    @Override
    public int getNumIdle() {
        return connectionPool.getNumIdle();
    }

    @Override
    public int getNumActive() {
        return connectionPool.getNumActive();
    }

    @Override
    public String getConnectionPoolName() {
        return connectionPool.getJmxName().getCanonicalName();
    }

    private void jmxRegister() {
        ObjectName objectName = null;
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        int i = 1;
        boolean registered = false;
        while (!registered) {
            try {
                ObjectName objName;
                // Skip the numeric suffix for the first pool in case there is
                // only one so the names are cleaner.
                if (i == 1) {
                    objName = new ObjectName(ONAME_BASE);
                } else {
                    objName = new ObjectName(ONAME_BASE + i);
                }
                mbs.registerMBean(this, objName);
                objectName = objName;
                registered = true;
            } catch (final MalformedObjectNameException | InstanceAlreadyExistsException e) {
                // Increment the index and try again
                i++;
            } catch (final MBeanRegistrationException | NotCompliantMBeanException e) {
                // Shouldn't happen. Skip registration if it does.
                registered = true;
            }
        }
        this.oname = objectName;
    }

    private void jmxUnregister() {
        if (oname != null) {
            try {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(
                        oname);
            } catch (final MBeanRegistrationException | InstanceNotFoundException e) {
                // swallow exception
            }
        }
    }

    public String getId() {
        return this.id;
    }
}
