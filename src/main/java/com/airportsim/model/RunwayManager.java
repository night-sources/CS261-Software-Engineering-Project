package com.airportsim.model;

import com.airportsim.viewmodel.RunwaySnapshot;
import com.airportsim.viewmodel.RunwaysSnapshot;
import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;
import java.util.ArrayList;
import java.util.List;

public class RunwayManager implements Tickable, SnapshotFactory {
    private List<Runway> runways;

    public RunwayManager() {
        this.runways = new ArrayList<>();
    }

    public RunwayManager(List<Runway> runways) {
        this.runways = new ArrayList<>(runways);
    }

    public void addRunway(Runway runway) {
        if (runway == null) {
            throw new IllegalArgumentException("Runway cannot be null");
        }
        runways.add(runway);
    }

    /**
     * Allocation priority is as follows: single-use (compatible) runway > mixed-mode runway > no
     * runway
     *
     * @param aircraft the aircraft to attempt to land
     * @return false if all compatible runways are busy
     */
    public boolean allocateRunway(Aircraft aircraft, long currentTick) {
        boolean needsLanding = aircraft.isInbound();
        RunwayMode preferredMode = needsLanding ? RunwayMode.LANDING : RunwayMode.TAKEOFF;

        for (Runway runway : runways) {
            if (runway.isAvailable() && runway.getMode() == preferredMode) {
                runway.occupy(aircraft, currentTick);
                return true;
            }
        }

        for (Runway runway : runways) {
            if (runway.isAvailable() && runway.getMode() == RunwayMode.MIXED) {
                runway.occupy(aircraft, currentTick);
                return true;
            }
        }

        return false;
    }

    public void setRunwayStatus(long id, OperationalStatus status) {
        runways.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .ifPresent(r -> r.setStatus(status));
    }

    public void setRunwayMode(long id, RunwayMode mode) {
        runways.stream().filter(r -> r.getId() == id).findFirst().ifPresent(r -> r.setMode(mode));
    }

    public List<Runway> getRunways() {
        return runways;
    }

    public Runway getRunwayById(long id) {
        if (runways == null) {
            return null;
        }

        for (Runway runway: runways) {
            if (runway.getId() == id)
                return runway;
        }

        return null;
    }

    @Override
    public void tick(long currentTick) {
        for (Runway runway : runways) {
            runway.tick(currentTick);
        }
    }

    @Override
    public Snapshot getSnapshot() {
        List<RunwaySnapshot> snapshots =
                runways.stream().map(runway -> (RunwaySnapshot) runway.getSnapshot()).toList();
        return new RunwaysSnapshot(snapshots);
    }
}
