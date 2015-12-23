package bgu.spl.app.Active;

import bgu.spl.app.Messages.PurchaseOrderRequest;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.Receipt;
import bgu.spl.app.Passive.Store;
import bgu.spl.mics.MicroService;

/**
 * Created by Amir on 23/12/2015.
 */
public class SellingService extends MicroService {

    private int currentTick ;

    public SellingService(String name){
        super(name);
        currentTick = 0 ;
    }

    @Override
    protected void initialize() {
        //updating currTick
        subscribeBroadcast(TickBroadcast.class,c1 -> this.currentTick = c1.getCurrentTick() );


        subscribeRequest(PurchaseOrderRequest.class,c -> {
            Store myStore = Store.getInstance();
            Store.BuyResult result = myStore.take(c.getShoeType(),c.isOnlyDiscount());
            if (result == Store.BuyResult.REGULAR_PRICE){
                Receipt r = new Receipt(super.getName(),c.getBuyer(),c.getShoeType(),false,this.currentTick,c.getRequestTick(),1);
                myStore.file(r);
                complete(c,r);
            }
            else if (result == Store.BuyResult.DISCOUNTED_PRICE) {
                Receipt r = new Receipt(super.getName(),c.getBuyer(),c.getShoeType(),true,this.currentTick,c.getRequestTick(),1);
                myStore.file(r);
                complete(c,r);
            }
            else if(result == Store.BuyResult.NOT_ON_DISCOUNT){
                complete(c,null);
            }
            else{ //result == Store.BuyResult.NOT_IN_STOCK

            }
        });
    }
}
