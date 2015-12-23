package bgu.spl.app.Active;

import bgu.spl.app.Messages.NewDiscountBroadcast;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.PurchaseSchedule;
import bgu.spl.mics.MicroService;

import java.util.List;
import java.util.Set;

/**
 * Created by Amir on 23/12/2015.
 */
public class WebsiteClientService extends MicroService {


    private int currentTick ;
    private List<PurchaseSchedule> list;
    private Set<String> set;

    public WebsiteClientService(String name, List<PurchaseSchedule> list, Set<String> set){
        super(name);
        currentTick = 0 ;
        this.list = list;
        this.set = set;
    }

    @Override
    protected void initialize() {
        //updating currTick
        subscribeBroadcast(TickBroadcast.class, c -> this.currentTick = c.getCurrentTick() );

        subscribeBroadcast(NewDiscountBroadcast.class,c -> {
            for(String shoes : set){

            }
        });


    }
}
