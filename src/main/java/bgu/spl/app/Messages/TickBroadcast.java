package bgu.spl.app.Messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;

/**
 * Created by Amir on 22/12/2015.
 */
public class TickBroadcast implements Broadcast {
    protected int currentTick = 0;

    public TickBroadcast(int currentTick) {
        this.currentTick = currentTick;
    }

    public int getCurrentTick() {
        return currentTick;
    }
}
