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
    private int currentTime;
    private CountDownLatch cdl;
    private CountDownLatch ender;

    public TimeService(int speed, int duration , CountDownLatch cdl, CountDownLatch ender) {
        super("Timer");
        this.cdl = cdl;
        if(speed<=0 && duration<=0)
            throw new RuntimeException("TimeService Arguments must be valid");
        this.speed = speed;
        this.duration = duration;
        this.ender = ender;
        currentTime = 0;
    }

    @Override
    protected void initialize() {
        try {
            cdl.await();
            LOGGER.info("System has been Started !");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subscribeBroadcast(TerminationBroadcast.class, o -> {
            terminate();
            ender.countDown();
        });

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime++;
                sendBroadcast(new TickBroadcast(currentTime));
                LOGGER.info("Tick: " + currentTime);
                if (currentTime >= duration) {
                    LOGGER.info("Terminating System !");
                    sendBroadcast(new TerminationBroadcast());
                    t.cancel();

                    try {
                        ender.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Store.getInstance().print();
                }
            }
        }, 0, speed);
    }

}
