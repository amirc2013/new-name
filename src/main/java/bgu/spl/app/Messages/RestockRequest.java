package bgu.spl.app.Messages;

import bgu.spl.mics.Message;
import bgu.spl.mics.Request;

/**
 * Created by Amir on 22/12/2015.
 */
public class RestockRequest implements Request<Boolean> {
    protected String shoeType;

    public RestockRequest(String shoeType) {
        this.shoeType = shoeType;
    }

    public String getShoeType() {
        return shoeType;
    }
}
