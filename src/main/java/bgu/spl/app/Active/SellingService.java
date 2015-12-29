package bgu.spl.app.Active;

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

    public SellingService(String name, CountDownLatch cdl){
        super(name);
        this.cdl = cdl;
        currentTick = 0 ;
        LOGGER.info(this.getName()+" is here to serve our customer !");
    }

    @Override
    protected void initialize() {
        //updating currTick
        subscribeBroadcast(TickBroadcast.class,c -> this.currentTick = c.getCurrentTick() );
        subscribeRequest(PurchaseOrderRequest.class,this::handlePurchaseOrder);
        subscribeBroadcast(TerminationBroadcast.class, o -> terminate());
        cdl.countDown();
    }

    private void handlePurchaseOrder(PurchaseOrderRequest c){
        try {
            Store myStore = Store.getInstance();
            Store.BuyResult result = myStore.take(c.getShoeType(), c.isOnlyDiscount());
            if (result == Store.BuyResult.REGULAR_PRICE) {
                Receipt r = new Receipt(super.getName(), c.getBuyer(), c.getShoeType(), false, this.currentTick, c.getRequestTick(), 1);
                myStore.file(r);
                complete(c, r);
                LOGGER.info(c.getBuyer() + " has bought " + c.getShoeType() + " successfully without Discount");
            } else if (result == Store.BuyResult.DISCOUNTED_PRICE) {
                Receipt r = new Receipt(super.getName(), c.getBuyer(), c.getShoeType(), true, this.currentTick, c.getRequestTick(), 1);
                myStore.file(r);
                complete(c, r);
                LOGGER.info(c.getBuyer() + " has bought " + c.getShoeType() + " successfully with Discount ! woohoo");
            } else if (result == Store.BuyResult.NOT_ON_DISCOUNT) {
                complete(c, null);
                LOGGER.info(c.getBuyer() + " has not bought " + c.getShoeType() + " since it does not have anymore Discount ");
            } else { //result == Store.BuyResult.NOT_IN_STOCK
                LOGGER.info("Sending restock request for : " + c.getShoeType());
                sendRequest(new RestockRequest(c.getShoeType()), c1 ->
                {
                    if (c1) {
                        Receipt r = new Receipt(super.getName(), c.getBuyer(), c.getShoeType(), false, this.currentTick, c.getRequestTick(), 1);
                        myStore.file(r);
                        complete(c, r);
                        LOGGER.info(c.getBuyer() + " has bought " + c.getShoeType() + " successfully without Discount (after Restock)");
                    } else {
                        complete(c, null);
                        LOGGER.info(c.getBuyer() + " has not bought " + c.getShoeType() + " since we dont have shoes of that kind left");
                    }
                });
            }
        } catch (Exception e){
            LOGGER.info(e.getMessage());
        }

    }

}
