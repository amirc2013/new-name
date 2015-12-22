package bgu.spl.app.Json;

import java.util.List;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class CustomerInfo {
    protected String name;
    protected List<String> wishList;
    protected List<PurchaseScheduleInfo> purchaseSchedule;

    public CustomerInfo() {
    }

    public CustomerInfo(String name, List<String> wishList, List<PurchaseScheduleInfo> purchaseSchedule) {
        this.name = name;
        this.wishList = wishList;
        this.purchaseSchedule = purchaseSchedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getWishList() {
        return wishList;
    }

    public void setWishList(List<String> wishList) {
        this.wishList = wishList;
    }

    public List<PurchaseScheduleInfo> getPurchaseSchedule() {
        return purchaseSchedule;
    }

    public void setPurchaseSchedule(List<PurchaseScheduleInfo> purchaseSchedule) {
        this.purchaseSchedule = purchaseSchedule;
    }
}
