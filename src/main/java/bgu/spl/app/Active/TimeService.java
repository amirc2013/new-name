package bgu.spl.app.Active;

import bgu.spl.app.Messages.TerminationBroadcast;
import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.app.Passive.Store;
import bgu.spl.mics.MicroService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Created by Amir on 22/12/2015.
 */
public class TimeService extends MicroService{
    private int speed ;
    private int duration ;
    private volatile int currentTime;

    public TimeService(int speed, int duration , CountDownLatch cdl) {
        super("timer",cdl);
        if(speed<=0 && duration<=0)
            throw new RuntimeException("TimeService Arguments must be valid");
        this.speed = speed;
        this.duration = duration;
        currentTime = 0;
    }

    @Override
    protected void initialize() {
        try { // wait until all the services are ready !
            cdl.await();
            LOGGER.info("System has been Initialized !");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subscribeBroadcast(TerminationBroadcast.class, o -> terminate());
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime += 1;
                sendBroadcast(new TickBroadcast(currentTime));
                LOGGER.info("Broadcast : The time now is "+currentTime);
                if(currentTime >= duration){
                    LOGGER.info("Termination Broadcast");
                    LOGGER.info("TimeService terminating !");
                    sendBroadcast(new TerminationBroadcast());
                    t.cancel();
                    terminate();
                }
            }
        }, 0, speed);
    }

}
