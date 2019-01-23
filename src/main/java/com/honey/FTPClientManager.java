package com.honey;

public interface FTPClientManager extends AutoCloseable {

    PooledFTPClient getFTPClient() throws Exception;

}
