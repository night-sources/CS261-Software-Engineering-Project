package com.airportsim.model;

import java.util.concurrent.atomic.AtomicLong;

import com.airportsim.viewmodel.AircraftSnapshot;
import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;

public class Aircraft implements SimulationEventSubject, SnapshotFactory {
    private final long id;
    private final String callsign;
    private final String operator;
    private final String origin;
    private final String destination;
    private final double groundSpeed;
    private final long scheduledTime;
    private final boolean inbound;

    private float altitude;
    private long fuelRemaining;
    private EmergencyStatus status;
    private long actualTime;

    private static final AtomicLong ID_GEN = new AtomicLong(0);

    public Aircraft(
            String callsign,
            String operator,
            String origin,
            String destination,
            long fuelRemaining,
            EmergencyStatus status,
            long scheduledTime,
            long actualTime,
            boolean inbound) {
        this.id = ID_GEN.getAndIncrement();
        this.callsign = callsign;
        this.operator = operator;
        this.origin = origin;
        this.destination = destination;
        this.groundSpeed = 180.0;
        this.altitude = 2000.0f;
        this.fuelRemaining = fuelRemaining;
        this.status = status;
        this.scheduledTime = scheduledTime;
        this.actualTime = actualTime;
        this.inbound = inbound;
    }

    public long getFuelRemaining() {
        return fuelRemaining;
    }

    /**
     * @param amount the amount of fuel to subtract from aircraft's holding
     */
    public void consumeFuel(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Fuel consumption amount cannot be negative");
        }
        this.fuelRemaining = Math.max(0, this.fuelRemaining - amount);
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public void declareEmergency(EmergencyStatus status) {
        this.status = status;
    }

    public boolean isEmergency() {
        return status != EmergencyStatus.NONE;
    }

    /**
     * We assume that an aircraft will immediately end up in one of the `AircraftQueue`s upon
     * creation
     *
     * @param currentTick active tick of the simulation
     * @return the total time the aircraft has spent waiting since actual creation
     */
    public long getWaitTime(long currentTick) {
        return currentTick - actualTime;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    public long getActualTime() {
        return actualTime;
    }

    public String getCallsign() {
        return callsign;
    }

    public String getOperator() {
        return operator;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public double getGroundSpeed() {
        return groundSpeed;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public boolean isInbound() {
        return inbound;
    }

    @Override
    public Snapshot getSnapshot() {
        return new AircraftSnapshot(
                id,
                callsign,
                operator,
                origin,
                destination,
                groundSpeed,
                altitude,
                fuelRemaining,
                status,
                scheduledTime,
                actualTime,
                inbound);
    }
}
