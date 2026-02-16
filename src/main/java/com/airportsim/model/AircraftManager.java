package com.airportsim.model;

import com.airportsim.viewmodel.AircraftSnapshot;
import com.airportsim.viewmodel.AircraftsSnapshot;
import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AircraftManager implements Tickable, SnapshotFactory {
    private final RunwayManager runwayManager;
    private final HoldingPattern holdingPattern;
    private final TakeoffQueue takeoffQueue;
    private final List<Aircraft> activeAircraft;

    private int inboundTimer;
    private int outboundTimer;
    private Random random;

    public AircraftManager(
            RunwayManager runwayManager, HoldingPattern holdingPattern, TakeoffQueue takeoffQueue) {
        this.runwayManager = runwayManager;
        this.holdingPattern = holdingPattern;
        this.takeoffQueue = takeoffQueue;
        this.activeAircraft = new ArrayList<>();
        this.random = new Random();
        this.inboundTimer = 0;
        this.outboundTimer = 0;
    }

    /**
     * Convenience constructor for simpler wiring.
     * Creates default HoldingPattern and TakeoffQueue.
     */
    public AircraftManager(RunwayManager runwayManager) {
        this(
                runwayManager,
                new HoldingPattern(new EmergencyTimeComparator()),
                new TakeoffQueue()
        );
    }

    public void spawnInbound() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void spawnOutbound() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void checkFuelLevels() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setEmergencyStatus(String callsign, EmergencyStatus status) {
        activeAircraft.stream()
                .filter(a -> a.getCallsign().equals(callsign))
                .findFirst()
                .ifPresent(a -> a.declareEmergency(status));
    }

    public void setSeed(long seed) {
        this.random = new Random(seed);
    }

    public HoldingPattern getHoldingPattern() {
        return holdingPattern;
    }

    public TakeoffQueue getTakeoffQueue() {
        return takeoffQueue;
    }

    public List<Aircraft> getActiveAircraft() {
        return activeAircraft;
    }

    @Override
    public void tick(long currentTick) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Snapshot getSnapshot() {
        List<AircraftSnapshot> snapshots =
                activeAircraft.stream()
                        .map(aircraft -> (AircraftSnapshot) aircraft.getSnapshot())
                        .toList();
        return new AircraftsSnapshot(snapshots);
    }
}
