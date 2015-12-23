package bgu.spl.app.Messages;

import bgu.spl.app.Passive.Receipt;
import bgu.spl.mics.Message;
import bgu.spl.mics.Request;

/**
 * Created by Amir on 22/12/2015.
 */
public class PurchaseOrderRequest implements Request<Receipt> {
    private String buyer;
    private String shoeType;
    private boolean onlyDiscount;
    private int requestTick ;


    public PurchaseOrderRequest(String buyer ,String shoeType, boolean onlyDiscount , int requestTick) {
        this.shoeType = shoeType;
        this.onlyDiscount = onlyDiscount;
        this.requestTick = requestTick;
        this.buyer = buyer;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getShoeType() {
        return shoeType;
    }

    public boolean isOnlyDiscount() {
        return onlyDiscount;
    }

    public int getRequestTick() {
        return requestTick;
    }
}
