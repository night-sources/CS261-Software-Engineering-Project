package com.airportsim.model;

import com.airportsim.viewmodel.AircraftsSnapshot;
import com.airportsim.viewmodel.RunwaysSnapshot;
import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;
import com.airportsim.viewmodel.StatisticsSnapshot;
import com.airportsim.viewmodel.WorldState;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SimulationEngine implements SnapshotFactory {
    private final List<Tickable> tickableManagers;
    private final List<SimulationEventListener> listeners;
    private final AircraftManager aircraftManager;
    private final RunwayManager runwayManager;
    private final StatisticsManager statisticsManager;
    private final Queue<SimulationCommand> pendingCommands;

    private long currentTick;
    private boolean isPaused;
    private double speedMultiplier;

    public SimulationEngine(
            AircraftManager aircraftManager,
            RunwayManager runwayManager,
            StatisticsManager statisticsManager) {
        this.aircraftManager = aircraftManager;
        this.runwayManager = runwayManager;
        this.statisticsManager = statisticsManager;

        this.tickableManagers = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.pendingCommands = new ConcurrentLinkedQueue<>();

        this.currentTick = 0;
        this.isPaused = true;
        this.speedMultiplier = 1.0;

        tickableManagers.add(aircraftManager);
        tickableManagers.add(runwayManager);

        listeners.add(statisticsManager);
    }

    public void update() {
        if (isPaused) {
            return;
        }

        processPendingCommands();

        for (Tickable tickable : tickableManagers) {
            tickable.tick(currentTick);
        }

        currentTick++;
    }

    private void processPendingCommands() {
        SimulationCommand command;
        while ((command = pendingCommands.poll()) != null) {
            command.execute(this);
        }
    }

    public void setRunwayOpStatus(int runwayId, OperationalStatus status) {
        pendingCommands.add(new RunwayOpStatusChangedCommand(runwayId, status));
    }

    public void setRunwayMode(int runwayId, RunwayMode mode) {
        pendingCommands.add(new RunwayModeChangedCommand(runwayId, mode));
    }

    public void setEmergencyStatus(String callsign, EmergencyStatus status) {
        pendingCommands.add(new EmergencyDeclaredCommand(callsign, status));
    }

    public AircraftManager getAircraftManager() {
        return aircraftManager;
    }

    public RunwayManager getRunwayManager() {
        return runwayManager;
    }

    public StatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public long getCurrentTick() {
        return currentTick;
    }

    public void registerListener(SimulationEventListener listener) {
        listeners.add(listener);
    }

    public void publishEvent(SimulationEvent event) {
        for (SimulationEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    @Override
    public Snapshot getSnapshot() {
        AircraftsSnapshot aircraftSnapshot = (AircraftsSnapshot) aircraftManager.getSnapshot();
        RunwaysSnapshot runwaysSnapshot = (RunwaysSnapshot) runwayManager.getSnapshot();
        StatisticsSnapshot statsSnapshot = (StatisticsSnapshot) statisticsManager.getSnapshot();

        return new WorldState(currentTick, aircraftSnapshot, runwaysSnapshot, statsSnapshot);
    }
}
