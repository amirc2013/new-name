package bgu.spl.app.Messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;

/**
 * Created by Amir on 22/12/2015.
 */
public class NewDiscountBroadcast implements Broadcast {

    private String shoeName ;
    private int amount;

    public String getShoeName() {
        return shoeName;
    }

    public int getAmount() {
        return amount;
    }

    public NewDiscountBroadcast(String shoeName, int amount) {
        this.amount = amount;

        this.shoeName = shoeName;
    }
}
