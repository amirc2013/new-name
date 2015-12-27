package bgu.spl.app;

import bgu.spl.app.Active.*;
import bgu.spl.app.Json.*;
import bgu.spl.app.Passive.DiscountSchedule;
import bgu.spl.app.Passive.PurchaseSchedule;
import bgu.spl.app.Passive.ShoeStorageInfo;
import bgu.spl.app.Passive.Store;
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

public class ShoeStoreRunner {

    Store store = Store.getInstance();

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
        CountDownLatch cdl = new CountDownLatch(info.getSellers()+info.getFactories()+info.getCustomers().size()+1);
        initManagerService(info.getManager(),cdl);
        initFactories(info.getFactories(),cdl);
        initSellers(info.getSellers(),cdl);
        initCustomers(info.getCustomers(),cdl);
        initTimerService(info.getTime(),cdl);
    }

    private void initCustomers(List<CustomerInfo> customers, CountDownLatch cdl) {
        ExecutorService service = Executors.newFixedThreadPool(customers.size());
        for(CustomerInfo c : customers){
            List<PurchaseSchedule> purchaseSchedules = c.getPurchaseSchedule().stream().map(PurchaseSchedule::new).collect(Collectors.toCollection(LinkedList::new));
            service.execute(new WebsiteClientService(c.getName(),purchaseSchedules, c.getWishList(),cdl));
        }
    }

    private void initSellers(int sellers, CountDownLatch cdl) {
        ExecutorService service = Executors.newFixedThreadPool(sellers);
        for(int i = 0 ; i < sellers ; i++)
            service.execute(new SellingService("SellingService"+i,cdl));
    }

    private void initFactories(int factories, CountDownLatch cdl) {
        ExecutorService service = Executors.newFixedThreadPool(factories);
        for(int i = 0 ; i < factories ; i++)
            service.execute(new ShoeFactoryService("ShoeFactoryService"+i,cdl));
    }

    private void initTimerService(TimeServiceInfo time, CountDownLatch cdl) {
        new Thread(new TimeService(time.getSpeed(),time.getDuration(),cdl)).start();
    }

    private void initManagerService(ManagerServiceInfo manager, CountDownLatch cdl) {
        List<DiscountSchedule> shoeStorageInfos = manager.getDiscountSchedule().stream().map(DiscountSchedule::new).collect(Collectors.toCollection(LinkedList::new));
        new Thread(new ManagementService(shoeStorageInfos,cdl)).start();
    }


}
