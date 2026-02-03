package com.airportsim.model;

import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;

public class Runway implements SnapshotFactory, SimulationEventSubject {
    private int id;
    private RunwayMode mode;
    private OperationalStatus status;
    private long occupiedUntil;
    private Aircraft occupiedBy;

    public boolean isAvailable() {}

    public void occupy(Aircraft aircraft) {}

    public void tick() {}

    public void setStatus(OperationalStatus status) {}

    @Override
    public Snapshot getSnapshot() {}
}
