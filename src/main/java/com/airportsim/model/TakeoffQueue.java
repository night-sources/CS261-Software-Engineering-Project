package com.airportsim.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class TakeoffQueue {
    private Queue<Aircraft> takeoffQueue;

    public TakeoffQueue() {
        this.takeoffQueue = new PriorityQueue<Aircraft>();
    }

    public void add(Aircraft aircraft) {
        takeoffQueue.add(aircraft);
    }

    public Aircraft poll() {
        return takeoffQueue.poll();
    }

    // Newly added method to ease testing
    public int size() {
        return takeoffQueue.size();
    }

    public List<Aircraft> removeExpired(int maxWait, long currentTick) {
        // if waiting time >= maxwait, remove plane from queue (flight cancelled)
        List<Aircraft> cancelled = new ArrayList<>();

        Iterator<Aircraft> it = takeoffQueue.iterator();
        
        while(it.hasNext()) {
            Aircraft aircraft = it.next();

            if (aircraft.getWaitTime(currentTick) >= maxWait) {
                cancelled.add(aircraft);
                it.remove();
            }
        }

        return cancelled;
    }
}
