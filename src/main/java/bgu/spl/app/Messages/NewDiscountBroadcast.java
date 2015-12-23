package bgu.spl.app.Messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;

/**
 * Created by Amir on 22/12/2015.
 */
public class NewDiscountBroadcast implements Broadcast {

    private String shoeName ;

    public String getShoeName() {
        return shoeName;
    }

    public NewDiscountBroadcast(String shoeName) {

        this.shoeName = shoeName;
    }
}
