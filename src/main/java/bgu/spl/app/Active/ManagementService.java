package bgu.spl.app.Active;

import bgu.spl.Exception.NoDiscountedShoe;
import bgu.spl.app.Messages.*;
import bgu.spl.app.Passive.DiscountSchedule;
import bgu.spl.app.Passive.Receipt;
import bgu.spl.app.Passive.Store;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by matan_000 on 23/12/2015.
 */
public class ManagementService extends MicroService {

    int currentTick;
    Map<String,Integer> restockRequests = new HashMap<>();
    Set<RestockRequest> recentRequests = new HashSet<>();

    Store store = Store.getInstance();

    List<DiscountSchedule> schedule;
    private CountDownLatch cdl;

    public ManagementService(List<DiscountSchedule> schedule, CountDownLatch cdl) {
        super("manager");
        this.schedule = schedule;
        this.cdl = cdl;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, this::handleTick);
        subscribeBroadcast(TerminationBroadcast.class, o -> terminate());
        subscribeRequest(RestockRequest.class, this::handleRestockRequest);
        cdl.countDown();
    }

    private void handleTick(TickBroadcast broadcast) {
        try {
            currentTick = broadcast.getCurrentTick();
            Optional<DiscountSchedule> s = schedule
                    .stream()
                    .filter(
                            sch -> sch.getTick() == broadcast.getCurrentTick())
                    .findFirst();

            if (s.isPresent()) {
                Store store = Store.getInstance();
                store.addDiscount(s.get().getShoeType(), s.get().getAmount());
                sendBroadcast(new NewDiscountBroadcast(s.get().getShoeType(), s.get().getAmount()));
                LOGGER.info("Broadcast : we have a 50% discount on : " + s.get().getShoeType());
            }
        }
        catch (NoDiscountedShoe e){
            e.printStackTrace();
        }
    }

    private void handleRestockRequest(RestockRequest r) {
        try {
            LOGGER.info("Handling Restock Request");
            recentRequests.add(r);
            if (!restockRequests.containsKey(r.getShoeType()))
                restockRequests.put(r.getShoeType(), -1);
            restockRequests.put(r.getShoeType(), restockRequests.get(r.getShoeType()) - 1);
            if (restockRequests.get(r.getShoeType()) < 0) {
                int requestAmount = currentTick % 5 + 1;
                Request<Receipt> request = new ManufacturingOrderRequest(r.getShoeType(), requestAmount, currentTick);
                boolean b = sendRequest(request, this::onManufacturingOrderRequestCompleted);
                if (b) {
                    restockRequests.put(r.getShoeType(), restockRequests.get(r.getShoeType()) + requestAmount);
                    if (requestAmount > 1)
                        LOGGER.info("We restocked " + requestAmount + " " + r.getShoeType() + " , be aware that some of the shoes are kept for someone else already");
                } else {
                    LOGGER.info("No restock for : " + r.getShoeType());
                    complete(r, false);
                }
            }
        } catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    private void onManufacturingOrderRequestCompleted(Receipt receipt) {
        LOGGER.info("Manufacturing Order Request Completed");
        store.add(receipt.getShoeType(),receipt.getAmountSold()-recentRequests.size());
        store.file(receipt);
        for(RestockRequest r : recentRequests){
            complete(r,true);
        }
        recentRequests.clear();
    }
}
