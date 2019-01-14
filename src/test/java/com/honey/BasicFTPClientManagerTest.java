package com.honey;

import org.junit.Assert;
import org.junit.Test;

public class BasicFTPClientManagerTest {

    @Test
    public void test() throws Exception {
        BasicFTPClientManager manager = new BasicFTPClientManager();
        manager.setHost("127.0.0.1");
        manager.setPort(21);
        manager.setUserName("root");
        manager.setPassword("123456");
        manager.setInitialSize(2);
        //Assert.assertTrue(manager.getNumActive() == 0);
        //Assert.assertTrue(manager.getNumIdle() == 2);

        PooledFTPClient ftpClient = manager.getFTPClient(); //borrow one from pool
        Assert.assertTrue(manager.getNumActive() == 1);
        Assert.assertTrue(manager.getNumIdle() == 1);

        ftpClient.disconnect(); //return to pool
        Assert.assertTrue(manager.getNumActive() == 0);
        Assert.assertTrue(manager.getNumIdle() == 2);
        manager.close();
    }
}
