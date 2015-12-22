package bgu.spl.app.Json;

import java.util.List;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class ExecutionInfo {
    protected List<StockInfo> initialStorage;
    protected ServicesInfo services;

    public ExecutionInfo() {
    }

    public ExecutionInfo(List<StockInfo> initialStorage, ServicesInfo services) {
        this.initialStorage = initialStorage;
        this.services = services;
    }

    public List<StockInfo> getInitialStorage() {
        return initialStorage;
    }

    public void setInitialStorage(List<StockInfo> initialStorage) {
        this.initialStorage = initialStorage;
    }

    public ServicesInfo getServices() {
        return services;
    }

    public void setServices(ServicesInfo services) {
        this.services = services;
    }
}
