package com.airportsim.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TakeoffQueue implements AircraftQueue {
    private Queue<Aircraft> takeoffQueue;

    public TakeoffQueue() {
        this.takeoffQueue = new LinkedList<>();
    }

    @Override
    public void add(Aircraft aircraft) {
        takeoffQueue.add(aircraft);
    }

    @Override
    public Aircraft poll() {
        return takeoffQueue.poll();
    }

    @Override
    public int size() {
        return takeoffQueue.size();
    }

    /**
     * @param maxWait the maximum time an aircraft will wait before cancellation
     * @param currentTick the active tick of the simulation
     * @return the list of aircraft after removing expired (in addition to performing inplace
     *     modification)
     */
    @Override
    public List<Aircraft> removeExpired(int maxWait, long currentTick) {
        List<Aircraft> cancelled = new ArrayList<>();
        Iterator<Aircraft> it = takeoffQueue.iterator();

        while (it.hasNext()) {
            Aircraft aircraft = it.next();
            if (aircraft.getWaitTime(currentTick) >= maxWait) {
                cancelled.add(aircraft);
                it.remove();
            }
        }

        return cancelled;
    }
}
