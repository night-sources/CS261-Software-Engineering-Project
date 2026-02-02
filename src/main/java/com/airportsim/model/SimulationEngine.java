package com.airportsim.model;

import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;

import java.util.List;

public class SimulationEngine implements SnapshotFactory {
    private List<Tickable> tickableManagers;
    private long currentTick;
    private boolean isPaused;
    private double speedMultiplier;
    private List<SimulationEventListener> listeners;
    private StatisticsManager statisticsManager;

    public SimulationEngine() {}

    public void init(SimulationConfig config) {}

    public void update() {}

    public void setPaused(boolean paused) {}

    public void setSpeedMultiplier(double speedMultiplier) {}

    public void setRunwayStatus(int runwayId, OperationalStatus status) {}

    public void registerListener(SimulationEventListener listener) {}

    public void publishEvent(SimulationEvent event) {}

    @Override
    public Snapshot getSnapshot() {}
}
