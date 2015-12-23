package bgu.spl.app.Active;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by אמיר on 22/12/2015.
 */
public class TimeService implements Runnable{

    private int speed ;
    private int duration ;
    private int currentTime;

    public TimeService(int speed, int duration) {
        if(speed<=0 && duration<=0)
            throw new RuntimeException("TimeService Arguments must be valid");
        this.speed = speed;
        this.duration = duration;
        currentTime = 1;
    }

    @Override
    public void run() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime += 1;
            }
        }, speed, speed*duration);
    }
}
