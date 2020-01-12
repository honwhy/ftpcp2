package com.github.honwhy;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BasicFTPClientManagerTest extends BasicFTPClientTestTemplate {

    @Test
    public void test() throws Exception {
        BasicFTPClientManager manager = new BasicFTPClientManager();
        manager.setHost("localhost");
        manager.setPort(getListenerPort());
        manager.setUsername(ADMIN_USERNAME);
        manager.setPassword(ADMIN_PASSWORD);
        manager.setInitialSize(2);
        manager.setMaxTotal(10);
        manager.setMaxWaitMillis(50L);
        //Assert.assertTrue(manager.getNumActive() == 0);
        //Assert.assertTrue(manager.getNumIdle() == 2);

        PooledFTPClient ftpClient = manager.getFTPClient(); //borrow one from pool
        Assert.assertEquals(manager.getNumActive(), 1);
        Assert.assertEquals(manager.getNumIdle(), 1);

        ftpClient.close(); //return to pool
        Assert.assertEquals(manager.getNumActive(), 0);
        Assert.assertEquals(manager.getNumIdle(), 2);
        List<PooledFTPClient> pooledFTPClientList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            pooledFTPClientList.add(manager.getFTPClient());
        }
        Assert.assertEquals(manager.getNumActive(), 10);
        Assert.assertEquals(manager.getNumIdle(), 0);
        try {
            manager.getFTPClient(); //can not get from pool anymore
            Assert.fail("would not get another one from pool");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (PooledFTPClient pooledFTPClient : pooledFTPClientList) {
            pooledFTPClient.close();
        }
        manager.close();
    }
}
