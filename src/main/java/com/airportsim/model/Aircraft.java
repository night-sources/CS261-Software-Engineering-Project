package com.airportsim.model;

import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;

public class Aircraft implements SimulationEventSubject, SnapshotFactory {
    private String callsign;
    private long fuelRemaining;
    private EmergencyStatus status;
    private long scheduledTime;

    public Aircraft() {}

    public void consumeFuel(long amount) {}

    public void declareEmergency(EmergencyStatus status) {}

    public int getWaitTime(long currentTick) {}

    public boolean isEmergency() {}

    @Override
    public Snapshot getSnapshot() {}
}
