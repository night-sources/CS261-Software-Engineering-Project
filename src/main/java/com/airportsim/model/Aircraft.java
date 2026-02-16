package com.airportsim.model;

import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;

public class Aircraft implements SimulationEventSubject, SnapshotFactory {
    private String callsign;
    private String operator;
    private String origin;
    private String destination;
    private double groundSpeed; // using double for decimal precision
    private float altitude;
    private long fuelRemaining;
    private EmergencyStatus status;
    private long scheduledTime; // need to consider for arrival and departure

    public Aircraft(
            String callsign,
            String operator,
            String origin,
            String destination,
            long fuelRemaining,
            EmergencyStatus status,
            long scheduledTime) {
        this.callsign = callsign;
        this.operator = operator;
        this.origin = origin;
        this.destination = destination;
        this.groundSpeed = 180;
        this.altitude = 2000;
        this.fuelRemaining = fuelRemaining;
        this.status = status;
        this.scheduledTime = scheduledTime;
    }

    // newly added method for HoldingPattern.java
    public long getFuelRemaining() {
        return fuelRemaining;
    }

    // newly added method for EmergencyTimeComparator.java
    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public void consumeFuel(long amount) {
        this.fuelRemaining -= amount;
    }

    public void declareEmergency(EmergencyStatus status) {
        this.status = status;
    }

    // *data type changed to long
    public long getWaitTime(long currentTick) {
        return currentTick - scheduledTime;
    }

    public boolean isEmergency() {
        return status == EmergencyStatus.FUEL_LOW
                || status == EmergencyStatus.MECHANICAL
                || status == EmergencyStatus.PASSENGER_HEALTH;
    }

    @Override
    public Snapshot getSnapshot() {}
}
