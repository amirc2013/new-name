package bgu.spl.app.Messages;

import bgu.spl.app.Passive.Receipt;
import bgu.spl.mics.Message;
import bgu.spl.mics.Request;

/**
 * Created by Amir on 22/12/2015.
 */
public class ManufacturingOrderRequest implements Request<Receipt> {
    protected String shoeType;

    public ManufacturingOrderRequest(String shoeType) {
        this.shoeType = shoeType;
    }

    public String getShoeType() {
        return shoeType;
    }
}
