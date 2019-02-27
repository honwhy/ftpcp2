package com.github.honwhy;

import java.io.Closeable;

public interface FTPClientManager extends Closeable {

    PooledFTPClient getFTPClient() throws Exception;

}
