package com.airportsim.model;

import com.airportsim.viewmodel.RunwaySnapshot;
import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;

public class Runway implements SnapshotFactory, SimulationEventSubject {
    private final int id;
    private final String runwayNumber;
    private final float length;
    private final float bearing;
    private RunwayMode mode;
    private OperationalStatus status;
    private long occupiedUntil;
    private Aircraft occupiedBy;

    public Runway(int id, String runwayNumber, float length, float bearing, RunwayMode mode) {
        this.id = id;
        this.runwayNumber = runwayNumber;
        this.length = length;
        this.bearing = bearing;
        this.mode = mode;
        this.status = OperationalStatus.AVAILABLE;
        this.occupiedUntil = 0;
        this.occupiedBy = null;
    }

    public int getId() {
        return id;
    }

    public String getRunwayNumber() {
        return runwayNumber;
    }

    public float getLength() {
        return length;
    }

    public float getBearing() {
        return bearing;
    }

    public RunwayMode getMode() {
        return mode;
    }

    public void setMode(RunwayMode mode) {
        this.mode = mode;
    }

    public OperationalStatus getStatus() {
        return status;
    }

    public void setStatus(OperationalStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == OperationalStatus.AVAILABLE && occupiedBy == null;
    }

    public void occupy(Aircraft aircraft, long currentTick) {
        this.occupiedBy = aircraft;
        // Calculate occupation time based on runway length and aircraft ground speed
        long occupationDuration = (long) (length / aircraft.getGroundSpeed());
        this.occupiedUntil = currentTick + occupationDuration;
    }

    public void tick(long currentTick) {
        if (occupiedBy != null && currentTick >= occupiedUntil) {
            occupiedBy = null;
            occupiedUntil = 0;
        }
    }

    @Override
    public Snapshot getSnapshot() {
        String occupiedByCallsign = (occupiedBy != null) ? occupiedBy.getCallsign() : null;
        return new RunwaySnapshot(id, mode, status, occupiedByCallsign);
    }
}
