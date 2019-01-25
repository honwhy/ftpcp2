package com.github.honwhy;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BasicFTPClientManagerTest {

    @Test
    public void test() throws Exception {
        BasicFTPClientManager manager = new BasicFTPClientManager();
        manager.setHost("127.0.0.1");
        manager.setPort(21);
        manager.setUsername("root");
        manager.setPassword("123456");
        manager.setInitialSize(2);
        manager.setMaxTotal(10);
        manager.setMaxWaitMillis(50L);
        //Assert.assertTrue(manager.getNumActive() == 0);
        //Assert.assertTrue(manager.getNumIdle() == 2);

        PooledFTPClient ftpClient = manager.getFTPClient(); //borrow one from pool
        Assert.assertTrue(manager.getNumActive() == 1);
        Assert.assertTrue(manager.getNumIdle() == 1);

        ftpClient.close(); //return to pool
        Assert.assertTrue(manager.getNumActive() == 0);
        Assert.assertTrue(manager.getNumIdle() == 2);
        List<PooledFTPClient> pooledFTPClientList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            pooledFTPClientList.add(manager.getFTPClient());
        }
        Assert.assertTrue(manager.getNumActive() == 10);
        Assert.assertTrue(manager.getNumIdle() == 0);
        try {
            manager.getFTPClient(); //can not get from pool anymore
            Assert.fail("would not get another one from pool");
        } catch (Exception e) {
        }
        for (PooledFTPClient pooledFTPClient : pooledFTPClientList) {
            pooledFTPClient.close();
        }
        manager.close();
    }
}
