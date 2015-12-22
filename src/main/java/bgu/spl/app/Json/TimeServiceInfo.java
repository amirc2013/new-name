package bgu.spl.app.Json;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class TimeServiceInfo {
    protected int speed;
    protected int duration;

    public TimeServiceInfo() {
    }

    public TimeServiceInfo(int speed, int duration) {
        this.speed = speed;
        this.duration = duration;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
