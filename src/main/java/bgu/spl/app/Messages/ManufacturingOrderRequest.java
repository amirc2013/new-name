package bgu.spl.app.Messages;

import bgu.spl.app.Passive.Receipt;
import bgu.spl.mics.Message;
import bgu.spl.mics.Request;

/**
 * Created by Amir on 22/12/2015.
 */
public class ManufacturingOrderRequest implements Request<Receipt> {
    protected String shoeType;
    protected int amount;
    protected int issuedTick;

    public ManufacturingOrderRequest(String shoeType, int amount, int issuedTick) {
        this.shoeType = shoeType;
        this.amount = amount;
        this.issuedTick = issuedTick;
    }

    public String getShoeType() {
        return shoeType;
    }

    public int getAmount() {
        return amount;
    }

    public int getIssuedTick() {
        return issuedTick;
    }
}
