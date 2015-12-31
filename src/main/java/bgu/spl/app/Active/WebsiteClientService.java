package bgu.spl.app.Active;

import bgu.spl.app.Messages.NewDiscountBroadcast;
import bgu.spl.app.Messages.PurchaseOrderRequest;
import bgu.spl.app.Messages.TerminationBroadcast;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.PurchaseSchedule;
import bgu.spl.app.Passive.Store;
import bgu.spl.mics.MicroService;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Amir on 23/12/2015.
 */
public class WebsiteClientService extends MicroService {


    private int currentTick ;
    private List<PurchaseSchedule> list;
    private Set<String> set;
    private CountDownLatch cdl;

    public WebsiteClientService(String name, List<PurchaseSchedule> list, Set<String> set, CountDownLatch cdl){
        super(name);
        this.cdl = cdl;
        currentTick = 0 ;
        this.list = list;
        this.set = set;
    }

    @Override
    protected void initialize() {
        //updating currTick
        subscribeBroadcast(TickBroadcast.class, this::handleTicks);
        subscribeBroadcast(NewDiscountBroadcast.class,this::handleDiscountBro);
        subscribeBroadcast(TerminationBroadcast.class, o -> terminate());
        LOGGER.info("Logging-in ...");
        cdl.countDown();
    }
    private void handleDiscountBro(NewDiscountBroadcast c){
            if(set.contains(c.getShoeName())){
                sendRequest(new PurchaseOrderRequest(getName(),c.getShoeName(),true,currentTick),c1 ->{
                    if(c1!=null){
                        set.remove(c.getShoeName());
                        quitIfNeeded();
                        //System.out.println(this.getName()+" has successfully got "+c.getShoeName());
                    }
                });
            }
    }

    private void handleTicks(TickBroadcast c){
        this.currentTick = c.getCurrentTick();

        list.stream().filter(ps -> ps.getTick() == c.getCurrentTick()).forEach(ps -> {
            LOGGER.info(String.format("Trying to buy a %s", ps.getShoeType()));
            sendRequest(new PurchaseOrderRequest(getName(), ps.getShoeType(), false, currentTick), r -> {
                        if (r != null) {
                            LOGGER.info(String.format("Purchased a %s, and got receipt: %s",r.getShoeType(),r.toString()));
                            list.remove(ps);
                            quitIfNeeded();
                        }
                    }
            );
        });
    }

    void quitIfNeeded() {
        if(set.isEmpty() && list.isEmpty()){
            LOGGER.info("Purchased Everything I Wanted, Logging-out ...");
            terminate();
        }
    }
}
