package com.honey;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

public class PoolableConnectionFactory implements PooledObjectFactory<PooledFTPClient> {

    FTPClientManagerConfig managerConfig;
    //nullable
    GenericObjectPool<PooledFTPClient> connectionPool;
    PoolableConnectionFactory(FTPClientManagerConfig managerConfig) {
        this.managerConfig = managerConfig;
    }

    @Override
    public PooledObject<PooledFTPClient> makeObject() throws Exception {
        Utils.checkNonNull(connectionPool,"connection pool is null, this SHOULD NOT HAPPEN!!!");
        PooledFTPClient client = configure();
        client.setConnectionPool(connectionPool);
        String host = managerConfig.host;
        int port = managerConfig.port;
        String userName = managerConfig.userName;
        String password = managerConfig.password;
        try {
            client.connect(host, port);
            int reply = client.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)) {
                client.superDisconnect();
                throw new Exception("failed to connect server" + host + ":" + port + " reply code is: " + reply);
            }
            if(userName != null && password != null) {
                client.login(userName, password);
                reply = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(reply)) {
                    client.logout();
                    client.superDisconnect();
                    throw new Exception("failed to login to server" + host + ":" + port + " reply code is: " + reply);
                }
            }
            //configure data connection mode
            String dataConnectionMode = managerConfig.dataConnectionMode;
            if (dataConnectionMode != null) {
                switch (dataConnectionMode) {
                    case "localActive":
                        client.enterLocalActiveMode();
                        break;
                    case "localPassive":
                        client.enterLocalPassiveMode();
                        break;
                    case "remoteActive":
                        client.enterRemoteActiveMode(InetAddress.getByName(managerConfig.host), managerConfig.port);
                        break;
                    case "remotePassive":
                        client.enterRemotePassiveMode();
                        break;
                    default:
                        client.enterLocalPassiveMode();
                }
            }
        } catch (SocketException se) {
            throw new Exception("socket exception happened while communicating with server " + host + ":" + port, se);
        } catch (IOException ioe) {
            throw new Exception("io exception happened while communicating with server " + host + ":" + port, ioe);
        }
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<PooledFTPClient> p) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<PooledFTPClient> p) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<PooledFTPClient> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<PooledFTPClient> p) throws Exception {

    }
    private PooledFTPClient configure() throws Exception {
        FTPClientConfig config = new FTPClientConfig();
        if(managerConfig.serverTimeZoneId != null) {
            config.setServerTimeZoneId(managerConfig.serverTimeZoneId);
        }

        PooledFTPClient client = new PooledFTPClient(connectionPool);
        client.configure(config);
        if(managerConfig.bufferSize != null) {
            client.setBufferSize(managerConfig.bufferSize);
        }
        if(managerConfig.controlEncoding != null) {
            client.setControlEncoding(managerConfig.controlEncoding);
        }
        if(managerConfig.keepAliveTimeout != null) {
            client.setControlKeepAliveTimeout(managerConfig.keepAliveTimeout);
        }
        if(managerConfig.controlKeepAliveReplyTimeout != null) {
            client.setControlKeepAliveReplyTimeout(managerConfig.controlKeepAliveReplyTimeout );
        }
        if(managerConfig.connectTimeout != null) {
            client.setConnectTimeout(managerConfig.connectTimeout);
        }

        return client;
    }


}
