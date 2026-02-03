package com.airportsim.model;

import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;
import java.util.List;

public class RunwayManager implements Tickable, SnapshotFactory {
    private List<Runway> runways;

    public RunwayManager() {}

    public boolean allocateRunway(Aircraft aircraft) {}

    public void setRunwayStatus(int id, OperationalStatus status) {}

    @Override
    public void tick(long currentTick) {}

    @Override
    public Snapshot getSnapshot() {}
}
