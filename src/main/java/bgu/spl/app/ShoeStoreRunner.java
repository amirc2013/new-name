package bgu.spl.app;

import bgu.spl.app.Active.*;
import bgu.spl.app.Json.*;
import bgu.spl.app.Passive.DiscountSchedule;
import bgu.spl.app.Passive.PurchaseSchedule;
import bgu.spl.app.Passive.ShoeStorageInfo;
import bgu.spl.app.Passive.Store;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ShoeStoreRunner{

    Store store = Store.getInstance();
    CountDownLatch cdl;

    ExecutionInfo executionInfo;
    public ShoeStoreRunner(ExecutionInfo executionInfo) {
        this.executionInfo = executionInfo;
        execute();
    }

    public static void main(String[] args){
        if(args == null || args.length == 0)
            throw new RuntimeException("Expected Gson File Name");

        String gsonFilePath = args[0];
        Gson gson = new Gson();
        try {
            ExecutionInfo executionInfo = gson.fromJson(new FileReader(gsonFilePath), ExecutionInfo.class);
            new ShoeStoreRunner(executionInfo);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Gson file (%s) not found !",gsonFilePath));
        }
    }

    private void execute() {
        initStorage(executionInfo.getInitialStorage());
        initServices(executionInfo.getServices());
    }

    void initStorage(List<StockInfo> initialStorage){
        List<ShoeStorageInfo> shoeStorageInfos = initialStorage.stream().map(ShoeStorageInfo::new).collect(Collectors.toCollection(LinkedList::new));
        store.load(shoeStorageInfos.toArray(new ShoeStorageInfo[initialStorage.size()]));
    }

    void initServices(ServicesInfo info){
        cdl = new CountDownLatch(info.getSellers()+info.getFactories()+info.getCustomers().size()+1);
        initManagerService(info.getManager());
        initFactories(info.getFactories());
        initSellers(info.getSellers());
        initCustomers(info.getCustomers());
        initTimerService(info.getTime());
    }

    private void initCustomers(List<CustomerInfo> customers) {
        for(CustomerInfo c : customers){
            List<PurchaseSchedule> purchaseSchedules = c.getPurchaseSchedule().stream().map(PurchaseSchedule::new).collect(Collectors.toCollection(LinkedList::new));
            new Thread(new WebsiteClientService(c.getName(),purchaseSchedules, c.getWishList(),cdl)).start();
        }
    }

    private void initSellers(int sellers) {
        for(int i = 0 ; i < sellers ; i++)
            new Thread(new SellingService("SellingService" + i, cdl)).start();
    }

    private void initFactories(int factories) {
        for(int i = 0 ; i < factories ; i++)
            new Thread(new ShoeFactoryService("ShoeFactoryService" + i, cdl)).start();
    }

    private void initTimerService(TimeServiceInfo time) {
        new Thread(new TimeService(time.getSpeed(),time.getDuration(),cdl)).start();
    }

    private void initManagerService(ManagerServiceInfo manager) {
        List<DiscountSchedule> shoeStorageInfos = manager.getDiscountSchedule().stream().map(DiscountSchedule::new).collect(Collectors.toCollection(LinkedList::new));
        new Thread(new ManagementService(shoeStorageInfos,cdl)).start();
    }


}
