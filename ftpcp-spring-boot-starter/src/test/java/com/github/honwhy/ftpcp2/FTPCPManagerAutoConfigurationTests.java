package com.github.honwhy.ftpcp2;

import com.github.honwhy.BasicFTPClientManager;
import com.github.honwhy.FTPClientManager;
import com.github.honwhy.PooledFTPClient;
import org.apache.ftpserver.test.ClientTestTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FTPCPManagerAutoConfiguration.class})
public class FTPCPManagerAutoConfigurationTests extends ClientTestTemplate {

    @Autowired
    private FTPClientManager ftpClientManager;

    @Test
    public void contextLoads() throws Exception {
        PooledFTPClient ftpClient = ftpClientManager.getFTPClient();
        System.out.println(ftpClient.getCreateTimestamp());
        BasicFTPClientManager basicFTPClientManager = (BasicFTPClientManager) ftpClientManager;
        System.out.println(basicFTPClientManager.getNumActive());
        System.out.println(basicFTPClientManager.getNumIdle());
    }


    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    @Override
    protected boolean isConnectClient() {
        return false;
    }
}

