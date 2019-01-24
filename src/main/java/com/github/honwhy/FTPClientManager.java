package com.github.honwhy;

public interface FTPClientManager extends AutoCloseable {

    PooledFTPClient getFTPClient() throws Exception;

}
