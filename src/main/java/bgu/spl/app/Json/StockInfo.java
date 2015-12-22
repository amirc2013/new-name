package bgu.spl.app.Json;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class StockInfo {
    protected String shoeType;
    protected int amount;

    public StockInfo() {
    }

    public StockInfo(String shoeType, int amount) {
        this.shoeType = shoeType;
        this.amount = amount;
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
}
