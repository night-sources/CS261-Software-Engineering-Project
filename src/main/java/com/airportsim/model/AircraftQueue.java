package com.airportsim.model;

import java.util.List;

public interface AircraftQueue {
    public void add(Aircraft aircraft);

    public Aircraft poll();

    public List<Aircraft> removeExpired(int maxWait);
}
