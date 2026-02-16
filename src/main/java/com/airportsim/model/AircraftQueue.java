package com.airportsim.model;

import java.util.List;

public interface AircraftQueue {
    void add(Aircraft aircraft);

    Aircraft poll();

    int size();

    List<Aircraft> removeExpired(int maxWait, long currentTick);
}
