package com.airportsim.model;

import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;
import com.airportsim.viewmodel.StatisticsSnapshot;
import java.io.File;

public class StatisticsManager implements SimulationEventListener, SnapshotFactory {
    private double totalDelay;
    private int totalDiversions;
    private int cancellations;

    public StatisticsManager() {
        this.totalDelay = 0.0;
        this.totalDiversions = 0;
        this.cancellations = 0;
    }

    @Override
    public void onEvent(SimulationEvent event) {}

    public double getTotalDelay() {
        return totalDelay;
    }

    public int getTotalDiversions() {
        return totalDiversions;
    }

    public int getCancellations() {
        return cancellations;
    }

    @Override
    public Snapshot getSnapshot() {
        return new StatisticsSnapshot(totalDelay, totalDiversions, cancellations);
    }

    public File exportReport() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
