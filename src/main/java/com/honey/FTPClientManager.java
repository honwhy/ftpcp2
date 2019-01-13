package com.honey;

public interface FTPClientManager {

    PooledFTPClient getFTPClient() throws Exception;

    void close() throws Exception;
}
