package bgu.spl.mics.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoundRobinList<T> {
    int index = 0;
    List<T> list = new CopyOnWriteArrayList<>();

    public void add(T t){
        list.add(t);
    }

    public synchronized void remove(T t){
        if(list.contains(t) && list.indexOf(t) < index % list.size())
            index--;
        list.remove(t);
    }

    public synchronized T get(){
        return list.get(index++ % list.size());
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }
}
