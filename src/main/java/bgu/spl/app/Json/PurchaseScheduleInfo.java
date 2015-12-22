package bgu.spl.app.Json;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class PurchaseScheduleInfo {
    protected String shoeType;
    protected int tick;

    public PurchaseScheduleInfo() {
    }

    public PurchaseScheduleInfo(String shoeType, int tick) {
        this.shoeType = shoeType;
        this.tick = tick;
    }

    public String getShoeType() {
        return shoeType;
    }

    public void setShoeType(String shoeType) {
        this.shoeType = shoeType;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
