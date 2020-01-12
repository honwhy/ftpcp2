package com.github.honwhy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * feature like smart data source
 */
public class SmartFTPClientManager extends BasicFTPClientManager {
    /**
     * set default choose strategy
     */
    private SmartChooseStrategy chooseStrategy = () -> "1";

    Map<String, BasicFTPClientManager> map = new HashMap<>();

    public void addManager(BasicFTPClientManager manager) {
        map.put(manager.getId(), manager);
    }

    @Override
    public PooledFTPClient getFTPClient() throws Exception {
        String id = chooseStrategy.getId();
        BasicFTPClientManager manager = map.get(id);
        return manager.getFTPClient();
    }

    @Override
    public void close() {
        for (Map.Entry<String, BasicFTPClientManager> entry : map.entrySet()) {
            entry.getValue().close();
        }
    }
    public void setChooseStrategy(SmartChooseStrategy chooseStrategy) {
        this.chooseStrategy = chooseStrategy;
    }
}
