package bgu.spl.app.Json;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class DiscountScheduleInfo {
    protected String shoeType;
    protected int amount;
    protected int tick;

    public DiscountScheduleInfo() {
    }

    public DiscountScheduleInfo(String shoeType, int amount, int tick) {
        this.shoeType = shoeType;
        this.amount = amount;
        this.tick = tick;
    }

    public String getShoeType() {
        return shoeType;
    }

    public void setShoeType(String shoeType) {
        this.shoeType = shoeType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
