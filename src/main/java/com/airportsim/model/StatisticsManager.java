package com.airportsim.model;

import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;
import java.io.File;

public class StatisticsManager implements SimulationEventListener, SnapshotFactory {
    private double totalDelay;
    private int totalDiversions;
    private int cancellations;

    // ...

    public StatisticsManager() {}

    @Override
    public void onEvent(SimulationEvent event) {}

    @Override
    public Snapshot getSnapshot() {}

    public File exportReport() {}
}
