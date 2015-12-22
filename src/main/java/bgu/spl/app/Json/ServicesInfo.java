package bgu.spl.app.Json;

import java.util.List;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class ServicesInfo {
    protected TimeServiceInfo time;
    protected ManagerServiceInfo manager;
    protected int factories;
    protected int sellers;
    protected List<CustomerInfo> customers;

    public ServicesInfo() {
    }

    public ServicesInfo(TimeServiceInfo time, ManagerServiceInfo manager, int factories, int sellers, List<CustomerInfo> customers) {
        this.time = time;
        this.manager = manager;
        this.factories = factories;
        this.sellers = sellers;
        this.customers = customers;
    }

    public TimeServiceInfo getTime() {
        return time;
    }

    public void setTime(TimeServiceInfo time) {
        this.time = time;
    }

    public ManagerServiceInfo getManager() {
        return manager;
    }

    public void setManager(ManagerServiceInfo manager) {
        this.manager = manager;
    }

    public int getFactories() {
        return factories;
    }

    public void setFactories(int factories) {
        this.factories = factories;
    }

    public int getSellers() {
        return sellers;
    }

    public void setSellers(int sellers) {
        this.sellers = sellers;
    }

    public List<CustomerInfo> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerInfo> customers) {
        this.customers = customers;
    }
}
