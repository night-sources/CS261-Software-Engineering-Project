package com.airportsim.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class HoldingPattern implements AircraftQueue {
    private Queue<Aircraft> holdingQueue;
    private EmergencyTimeComparator comparator;

    public HoldingPattern(EmergencyTimeComparator comparator) {
        this.comparator = comparator;
        this.holdingQueue = new PriorityQueue<>(this.comparator);
    }

    @Override
    public void add(Aircraft aircraft) {
        holdingQueue.add(aircraft);
    }

    @Override
    public Aircraft poll() {
        return holdingQueue.poll();
    }

    @Override
    public int size() {
        return holdingQueue.size();
    }

    /**
     * @param maxWait the maximum time an aircraft will wait before diverting
     * @param currentTick the active tick of the simulation
     * @return the list of aircraft after removing expired (in addition to performing inplace
     *     modification)
     */
    @Override
    public List<Aircraft> removeExpired(int maxWait, long currentTick) {
        List<Aircraft> diverted = new ArrayList<>();
        Iterator<Aircraft> it = holdingQueue.iterator();

        while (it.hasNext()) {
            Aircraft aircraft = it.next();
            // Divert if fuel is critically low (10 minutes or less)
            if (aircraft.getFuelRemaining() <= 10) {
                diverted.add(aircraft);
                it.remove();
            }
        }

        return diverted;
    }

    /**
     * Ensure an aircraft is placed in the correct spot of holding pattern
     *
     * @param aircraft the aircarft to re-position in queue
     */
    public void updatePriority(Aircraft aircraft) {
        holdingQueue.remove(aircraft);
        aircraft.consumeFuel(10); // TODO: Agree on fuel rate appraoch

        if (aircraft.getFuelRemaining() <= 10) {
            aircraft.setStatus(EmergencyStatus.FUEL_LOW);
        }

        holdingQueue.add(aircraft);
    }
}
