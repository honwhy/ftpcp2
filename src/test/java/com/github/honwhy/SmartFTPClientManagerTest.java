package com.github.honwhy;

import org.apache.ftpserver.test.ClientTestTemplate;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SmartFTPClientManagerTest {

    static MyClientTestTemplate c1;
    static MyClientTestTemplate c2;

    @BeforeClass
    public static void setup() throws Exception {
        c1 = new MyClientTestTemplate();
        c1.setup();
        c2 = new MyClientTestTemplate();
        c2.setup();
    }

    @Test
    public void test_defaultChoose() throws Exception {
        SmartFTPClientManager smartFTPClientManager = new SmartFTPClientManager();
        BasicFTPClientManager m1 = new BasicFTPClientManager();
        m1.setHost("localhost");
        m1.setPort(c1.getListenerPort());
        m1.setUsername(MyClientTestTemplate.ADMIN_USERNAME);
        m1.setPassword(MyClientTestTemplate.ADMIN_PASSWORD);
        m1.setInitialSize(2);
        m1.setMaxTotal(10);
        m1.setMaxWaitMillis(50L);
        m1.setConnectTimeout(1000);

        BasicFTPClientManager m2 = new BasicFTPClientManager();
        m2.setHost("localhost");
        m2.setPort(c2.getListenerPort());
        m2.setUsername(MyClientTestTemplate.ADMIN_USERNAME);
        m2.setPassword(MyClientTestTemplate.ADMIN_PASSWORD);
        m2.setInitialSize(2);
        m2.setMaxTotal(10);
        m2.setMaxWaitMillis(50L);
        m2.setConnectTimeout(2000);

        smartFTPClientManager.addManager(m1);
        smartFTPClientManager.addManager(m2);

        PooledFTPClient pooledFTPClient = smartFTPClientManager.getFTPClient();
        Assert.assertEquals(pooledFTPClient.getConnectTimeout(), 1000);

        smartFTPClientManager.setChooseStrategy(new ThreadLocalChooseStrategy());
        ThreadLocalChooseStrategy.setId("2");
        PooledFTPClient p2 = smartFTPClientManager.getFTPClient();
        Assert.assertEquals(p2.getConnectTimeout(), 2000);
        smartFTPClientManager.close();
    }




    @AfterClass
    public static void teardown() throws Exception {
        c1.teardown();
        c2.teardown();
    }

    static class MyClientTestTemplate extends ClientTestTemplate {
        public static String ADMIN_USERNAME = ClientTestTemplate.ADMIN_USERNAME;
        public static String ADMIN_PASSWORD = ClientTestTemplate.ADMIN_PASSWORD;

        public void setup() throws Exception {
            super.setUp();
        }

        public void teardown() throws Exception {
            super.tearDown();
        }

        public int getListenerPort() {
            return super.getListenerPort();
        }
    }

    static class ThreadLocalChooseStrategy implements SmartChooseStrategy {
        private static ThreadLocal<String> t = ThreadLocal.withInitial(() -> "1");

        public static void setId(String id) {
            t.set(id);
        }
        @Override
        public String getId() {
            return t.get();
        }
    }
}
