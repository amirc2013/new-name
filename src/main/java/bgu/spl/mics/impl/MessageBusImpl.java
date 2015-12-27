package bgu.spl.mics.impl;

import bgu.spl.mics.*;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageBusImpl implements MessageBus {

    protected static MessageBus instance;
    private MessageBusImpl() {
    }

    public static MessageBus getInstance(){
        if(instance == null)
            instance = new MessageBusImpl();
        return instance;
    }

    final Map<MicroService,BlockingQueue<Message>> map = new ConcurrentHashMap<>();
    final Map<Class<? extends Request>,RoundRobinList<MicroService>> requestmap = new ConcurrentHashMap<>();
    final Map<Class<? extends Broadcast>,Set<MicroService>> broadcastmap = new ConcurrentHashMap<>();
    final Map<Request,MicroService> requestersmap = new ConcurrentHashMap<>();

    @Override
    public void subscribeRequest(Class<? extends Request> type, MicroService m) {
        synchronized (type){
            if(!requestmap.containsKey(type))
                requestmap.put(type, new RoundRobinList<>());
            requestmap.get(type).add(m);
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        synchronized (type){
            if(!broadcastmap.containsKey(type))
                broadcastmap.put(type,new CopyOnWriteArraySet<>());
            broadcastmap.get(type).add(m);
        }
    }

    @Override
    public <T> void complete(Request<T> r, T result) {
        if(map.containsKey(requestersmap.get(r))){
            map.get(requestersmap.get(r)).add(new RequestCompleted<>(r,result));
            requestersmap.remove(r);
        }
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        if(broadcastmap.containsKey(b.getClass()))
            for(MicroService m : broadcastmap.get(b.getClass()))
                try {
                    BlockingQueue<Message> q = map.get(m);
                    if(q == null)
                        throw new RuntimeException(m.getName()+" is not registered but trying to sendBroadcast");
                    q.put(b);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
    }

    @Override
    public boolean sendRequest(Request<?> r, MicroService requester) {
        if(!requestmap.containsKey(r.getClass()))
            return false;

        map.get(requestmap.get(r.getClass()).get()).add(r);
        requestersmap.put(r,requester);

        return true;
    }

    @Override
    public void register(MicroService m) {
        map.put(m,new LinkedBlockingQueue<>());
    }

    @Override
    public void unregister(MicroService m) {
        map.remove(m);
        for(Class<? extends Request> c : requestmap.keySet()){
            requestmap.get(c).remove(m);
            if(requestmap.get(c).isEmpty())
                requestmap.remove(c);
        }
        for(Class<? extends Broadcast> c : broadcastmap.keySet()){
            broadcastmap.get(c).remove(m);
            if(broadcastmap.get(c).isEmpty())
                broadcastmap.remove(c);
        }
    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        return map.get(m).take();
    }
}