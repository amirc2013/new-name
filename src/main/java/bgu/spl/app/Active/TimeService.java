package bgu.spl.app.Active;

import bgu.spl.app.Messages.TerminationBroadcast;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.Store;
import bgu.spl.mics.MicroService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by Amir on 22/12/2015.
 */
public class TimeService extends MicroService{
    private int speed ;
    private int duration ;
    private volatile int currentTime;

    public TimeService(int speed, int duration) {
        super("timer");
        if(speed<=0 && duration<=0)
            throw new RuntimeException("TimeService Arguments must be valid");
        this.speed = speed;
        this.duration = duration;
        currentTime = 1;
    }

    @Override
    protected void initialize() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime += 1;
                sendBroadcast(new TickBroadcast(currentTime));
                System.out.println("Broadcast : The time now is "+currentTime);
                if(currentTime >= duration){
                    sendBroadcast(new TerminationBroadcast());
                    System.out.println("Termination Broadcast");
                    System.out.println("TimeService terminating !");
                    Store store = Store.getInstance();
                    store.print();
                    t.cancel();
                    terminate();
                }
            }
        }, 0, speed);
    }

}
