package bgu.spl.app.Active;

import bgu.spl.app.Messages.NewDiscountBroadcast;
import bgu.spl.app.Messages.PurchaseOrderRequest;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.PurchaseSchedule;
import bgu.spl.app.Passive.Store;
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

        System.out.println(name + " has logged into the store !");
    }

    @Override
    protected void initialize() {
        Store myStore = Store.getInstance();

        //updating currTick
        subscribeBroadcast(TickBroadcast.class, this::handleTicks);
        subscribeBroadcast(NewDiscountBroadcast.class,this::handleDiscountBro);
    }
    private void handleDiscountBro(NewDiscountBroadcast c){
            if(set.contains(c.getShoeName())){
                sendRequest(new PurchaseOrderRequest(getName(),c.getShoeName(),true,currentTick),c1 ->{
                    if(c1!=null){
                        set.remove(c.getShoeName());
                        System.out.println(this.getName()+" has successfully bought "+c.getShoeName());
                    }
                });
            }
    }

    private void handleTicks(TickBroadcast c){
            this.currentTick = c.getCurrentTick();

            for(PurchaseSchedule ps : list){
                if(ps.getTick() == c.getCurrentTick()){
                    System.out.println(this.getName()+" is trying to buy "+ps.getShoeType());
                    sendRequest(new PurchaseOrderRequest(getName(),ps.getShoeType(),false,currentTick),c1 -> {
                            }
                    );
                }
            }
    }
}
