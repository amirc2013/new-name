package bgu.spl.app.Active;

import bgu.spl.app.Messages.TickBroadcast;
import bgu.spl.mics.MicroService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by אמיר on 22/12/2015.
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
            }
        }, speed, speed*duration);
    }

}
