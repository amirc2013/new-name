package bgu.spl.app.Active;

import bgu.spl.Exception.NotOwnTheShoe;
import bgu.spl.app.Messages.PurchaseOrderRequest;
import bgu.spl.app.Messages.RestockRequest;
import bgu.spl.app.Messages.TerminationBroadcast;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.Receipt;
import bgu.spl.app.Passive.Store;
import bgu.spl.mics.MicroService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created by Amir on 23/12/2015.
 */
public class SellingService extends MicroService {

    private int currentTick ;
    private CountDownLatch cdl;
    private CountDownLatch ender;
    Store myStore = Store.getInstance();

    public SellingService(String name, CountDownLatch cdl, CountDownLatch ender){
        super(name);
        this.cdl = cdl;
        this.ender = ender;
        currentTick = 0 ;
}

    @Override
    protected void initialize() {
        //updating currTick
        subscribeBroadcast(TickBroadcast.class, c -> this.currentTick = c.getCurrentTick());
        subscribeRequest(PurchaseOrderRequest.class,this::handlePurchaseOrder);
        subscribeBroadcast(TerminationBroadcast.class, o -> {
            terminate();
            ender.countDown();
        });
        cdl.countDown();
    }

    private void handlePurchaseOrder(PurchaseOrderRequest c){
        Store.BuyResult result = myStore.take(c.getShoeType(), c.isOnlyDiscount());
        switch (result){
            case REGULAR_PRICE:
            case DISCOUNTED_PRICE:{
                boolean b = result != Store.BuyResult.REGULAR_PRICE;
                buy(c,b);
                break;
            }
            case NOT_ON_DISCOUNT:{
                disregard(c);
                break;
            }
            case NOT_IN_STOCK:{
                LOGGER.info("Sending restock request for : " + c.getShoeType());
                sendRequest(new RestockRequest(c.getShoeType()), res ->{
                    if (res) {
                        buy(c, false);
                    } else {
                        disregard(c);
                    }
                });
                break;
            }
        }
    }

    void buy(PurchaseOrderRequest c , boolean discount){
        LOGGER.info(String.format("%s has bought %s successfully with%s discount",c.getBuyer(),c.getShoeType(),discount ? "":"out"));
        Receipt r = new Receipt(getName(), c.getBuyer(), c.getShoeType(), discount, currentTick, c.getRequestTick(), 1);
        myStore.file(r);
        complete(c, r);
    }

    void disregard(PurchaseOrderRequest c){
        LOGGER.info(String.format("%s did not buy %s since it does not have a discount",c.getBuyer(),c.getShoeType()));
        complete(c, null);
    }
}
