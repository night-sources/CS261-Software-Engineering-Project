package com.airportsim.model;

import java.util.List;
import java.util.Queue;

public class TakeoffQueue {
    private Queue<Aircraft> takeoffQueue;

    public TakeoffQueue() {}

    public void add(Aircraft aircraft) {}

    public Aircraft poll() {}

    public List<Aircraft> removeExpired(int maxWait) {}
}
