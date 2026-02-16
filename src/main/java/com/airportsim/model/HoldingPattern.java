package com.airportsim.model;

// import java.util.List;
// import java.util.Queue;
// import java.util.PriorityQueue;

import java.util.*;

public class HoldingPattern {
    private Queue<Aircraft> holdingQueue;
    private EmergencyTimeComparator comparator;

    public HoldingPattern() {
        this.comparator = new EmergencyTimeComparator();
        this.holdingQueue = new PriorityQueue<Aircraft>(comparator);
    }

    public void add(Aircraft aircraft) {
        // Add the aircraft to the priority queue
        holdingQueue.add(aircraft);
    }

    public Aircraft poll() {
        return holdingQueue.poll();
    }

    // Newly added method to ease testing
    public int size() {
        return holdingQueue.size();
    }

    public List<Aircraft> removeExpired() {
        // if fuelRemaining = 10 minutes, remove plane from queue(flight diverted)
        List<Aircraft> diverted = new ArrayList<>();

        Iterator<Aircraft> it = holdingQueue.iterator();

        while (it.hasNext()) {
            Aircraft aircraft = it.next();

            if (aircraft.getFuelRemaining() <= 10) {
                diverted.add(aircraft);
                it.remove();
            }
        }

        return diverted;
    }

    public void updatePriority(Aircraft aircraft) {
        // Delete the aircraft from queue and insert it back
        // So that the queue is properly prioritised
        holdingQueue.remove(aircraft);
        aircraft.consumeFuel(10); // 3.3.2 Design Document "Constant rate per tick" BUT what value?

        if (aircraft.getFuelRemaining() <= 10) {
            aircraft.setStatus(EmergencyStatus.FUEL_LOW);
        }

        holdingQueue.add(aircraft);
    }
}
