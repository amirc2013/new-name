package bgu.spl.app.Active;

import bgu.spl.app.Messages.ManufacturingOrderRequest;
import bgu.spl.app.Messages.TerminationBroadcast;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.Receipt;
import bgu.spl.mics.MicroService;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by matan_000 on 23/12/2015.
 */
public class ShoeFactoryService extends MicroService{

    Queue<ManufacturingOrderRequest> queue = new ArrayDeque<>();

    int completedShoes = 0;
    int currentTick;

    public ShoeFactoryService(String name) {
        super(name);
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,this::handleTickBroadcast);
        subscribeBroadcast(TerminationBroadcast.class, o -> terminate());
        subscribeRequest(ManufacturingOrderRequest.class, this::handleManufacturingOrderRequest);
    }

    private void handleTickBroadcast(TickBroadcast tickBroadcast) {
        currentTick = tickBroadcast.getCurrentTick();
        if(!queue.isEmpty()){
            completedShoes++;
            if(completedShoes >= queue.peek().getAmount()){
                Receipt r = new Receipt(getName(),"store",queue.peek().getShoeType(),false,currentTick,queue.peek().getIssuedTick(),queue.peek().getAmount());
                complete(queue.remove(),r);
                completedShoes = 0;
            }
        }
    }

    private void handleManufacturingOrderRequest(ManufacturingOrderRequest manufacturingOrderRequest) {
        queue.add(manufacturingOrderRequest);
    }
}
