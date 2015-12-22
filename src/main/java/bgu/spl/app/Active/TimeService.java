package bgu.spl.app.Active;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by אמיר on 22/12/2015.
 */
public class TimeService implements Runnable{

    private int speed ;
    private int duration ;

    public TimeService(int speed, int duration) {
        if(speed<=0 && duration<=0)
            throw new RuntimeException("TimeService Arguments must be valid");
        this.speed = speed;
        this.duration = duration;
    }

    @Override
    public void run() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 22);
    }
}
