package bgu.spl.app.Json;

import java.util.List;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class ManagerServiceInfo {
    protected List<DiscountScheduleInfo> discountSchedule;

    public ManagerServiceInfo() {
    }

    public ManagerServiceInfo(List<DiscountScheduleInfo> discountSchedule) {
        this.discountSchedule = discountSchedule;
    }

    public List<DiscountScheduleInfo> getDiscountSchedule() {
        return discountSchedule;
    }

    public void setDiscountSchedule(List<DiscountScheduleInfo> discountSchedule) {
        this.discountSchedule = discountSchedule;
    }
}
