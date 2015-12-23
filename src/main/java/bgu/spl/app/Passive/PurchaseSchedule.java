package bgu.spl.app.Passive;

/**
 * Created by Amir on 22/12/2015.
 */
public class PurchaseSchedule {

    // Fields;
    private String shoeType;
    private int tick; // The tick number to send the PurchaseOrderRequest at.


    public String getShoeType() {
        return shoeType;
    }

    public int getTick() {
        return tick;
    }


    public PurchaseSchedule(int tick, String shoeType) {
        this.tick = tick;
        this.shoeType = shoeType;
    }
}
