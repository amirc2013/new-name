package bgu.spl.app.Active;

import bgu.spl.app.Messages.ManufacturingOrderRequest;
import bgu.spl.app.Messages.NewDiscountBroadcast;
import bgu.spl.app.Messages.RestockRequest;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.DiscountSchedule;
import bgu.spl.app.Passive.Receipt;
import bgu.spl.app.Passive.Store;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;

import java.util.*;

/**
 * Created by matan_000 on 23/12/2015.
 */
public class ManagementService extends MicroService {

    int currentTick;
    Map<String,Integer> restockRequests = new HashMap<>();
    Set<RestockRequest> recentRequests = new TreeSet<>();

    Store store = Store.getInstance();

    List<DiscountSchedule> schedule;
    public ManagementService(List<DiscountSchedule> schedule) {
        super("manager");
        this.schedule = schedule;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, this::handleTick);
        subscribeRequest(RestockRequest.class, this::handleRestockRequest);
    }

    private void handleTick(TickBroadcast broadcast) {
        currentTick = broadcast.getCurrentTick();
        Optional<DiscountSchedule> s = schedule
                .stream()
                .filter(
                        sch -> sch.getTick() == broadcast.getCurrentTick())
                .findFirst();
        if(s.isPresent()){
            sendBroadcast(new NewDiscountBroadcast(s.get().getShoeType()));
            System.out.println("Broadcast : we have a 50% discount on : " + s.get().getShoeType());
        }
    }

    private void handleRestockRequest(RestockRequest r) {
        recentRequests.add(r);
        if(!restockRequests.containsKey(r.getShoeType()))
            restockRequests.put(r.getShoeType(),-1);
        restockRequests.put(r.getShoeType(),restockRequests.get(r.getShoeType())-1);
        if(restockRequests.get(r.getShoeType()) < 0 ){
            int requestAmount = currentTick % 5 + 1;
            Request<Receipt> request = new ManufacturingOrderRequest(r.getShoeType(),requestAmount,currentTick);
            boolean b = sendRequest(request, this::onManufacturingOrderRequestCompleted);
            if(b) {
                restockRequests.put(r.getShoeType(), restockRequests.get(r.getShoeType()) + requestAmount);
                System.out.println("We restock "+requestAmount+" "+r.getShoeType()+" but be aware that some of the shoes a kept for someone else");
            }
            else
                complete(r,false);
        }
    }

    private void onManufacturingOrderRequestCompleted(Receipt receipt) {
        store.add(receipt.getShoeType(),receipt.getAmountSold()-recentRequests.size());
        store.file(receipt);
        for(RestockRequest r : recentRequests){
            complete(r,true);
        }
        recentRequests.clear();
    }
}
