package com.airportsim.model;

import java.util.List;
import java.util.Queue;

public class HoldingPattern {
    private Queue<Aircraft> holdingQueue;
    private EmergencyTimeComparator comparator;

    public HoldingPattern() {}

    public void add(Aircraft aircraft) {}

    public Aircraft poll() {}

    public List<Aircraft> removeExpired() {}

    public void updatePriority(Aircraft aircraft) {}
}
