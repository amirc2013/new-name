package bgu.spl.app.Passive;

/**
 * Created by Amir on 22/12/2015.
 */
public class DiscountSchedule {

    // fields

    private String shoeType;
    private int tick;
    private int amount;

    public String getShoeType() {
        return shoeType;
    }

    public int getTick() {
        return tick;
    }

    public int getAmount() {
        return amount;
    }

    public DiscountSchedule(String shoeType, int tick, int amount) {

        this.shoeType = shoeType;
        this.tick = tick;
        this.amount = amount;
    }
}
