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
import java.util.concurrent.atomic.AtomicReference;

public class SimulationEngine implements SnapshotFactory {
    private static final long DEFAULT_TICK_INTERVAL_MS = 1000;

    private final List<Tickable> tickableManagers;
    private final List<SimulationEventListener> listeners;
    private final AircraftManager aircraftManager;
    private final RunwayManager runwayManager;
    private final StatisticsManager statisticsManager;
    private final Queue<SimulationCommand> pendingCommands;
    private final AtomicReference<WorldState> latestSnapshot;

    private long currentTick;
    private boolean isPaused;
    private boolean isRunning;
    private long tickIntervalMs;

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
        this.latestSnapshot = new AtomicReference<>();

        this.currentTick = 0;
        this.isPaused = true;
        this.isRunning = false;
        this.tickIntervalMs = DEFAULT_TICK_INTERVAL_MS;

        tickableManagers.add(aircraftManager);
        tickableManagers.add(runwayManager);

        listeners.add(statisticsManager);
    }

    /**
     * Starts the simulation loop. This method blocks and should be called
     * from a dedicated worker thread, not the UI thread
     * The loop continues until `stop()` is called
     */
    public void run() {
        isRunning = true;
        long lastTickTime = System.currentTimeMillis();

        // Publish initial snapshot so UI has something to render immediately
        latestSnapshot.set(createWorldState());

        while (isRunning) {
            long currentTime = System.currentTimeMillis();

            if (!isPaused && (currentTime - lastTickTime >= tickIntervalMs)) {
                update();
                latestSnapshot.set(createWorldState());
                lastTickTime = currentTime;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Stops the simulation loop. The loop will exit after completing
     * the current iteration.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Returns whether the simulation loop is currently running.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Executes a single simulation tick. Called internally by run(),
     * but can also be called directly for manual stepping or testing
     */
    public void update() {
        // Step 1: Process/execute pending commands from UI
        processPendingCommands();

        // Step 2: Tick all managers (aircraft spawning, fuel consumption, runway updates, etc.)
        for (Tickable tickable : tickableManagers) {
            tickable.tick(currentTick);
        }

        // Step 3: Advance simulation time
        currentTick++;
    }

    private void processPendingCommands() {
        SimulationCommand command;
        while ((command = pendingCommands.poll()) != null) {
            command.execute(this);
        }
    }

    /**
     * Returns the most recent snapshot for UI rendering
     * Thread-safe: can be called from the UI thread while simulation runs
     */
    public WorldState getLatestSnapshot() {
        return latestSnapshot.get();
    }

     // Creates a new WorldState snapshot of the current simulation state (helper for clarity)
    private WorldState createWorldState() {
        AircraftsSnapshot aircraftSnapshot = (AircraftsSnapshot) aircraftManager.getSnapshot();
        RunwaysSnapshot runwaysSnapshot = (RunwaysSnapshot) runwayManager.getSnapshot();
        StatisticsSnapshot statsSnapshot = (StatisticsSnapshot) statisticsManager.getSnapshot();

        return new WorldState(currentTick, aircraftSnapshot, runwaysSnapshot, statsSnapshot);
    }

    @Override
    public Snapshot getSnapshot() {
        return createWorldState();
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

    /**
     * Sets the interval between ticks in milliseconds
     * @param intervalMs milliseconds between ticks. Use 0 for maximum speed
     */
    public void setTickIntervalMs(long intervalMs) {
        this.tickIntervalMs = Math.max(0, intervalMs);
    }

    public long getTickIntervalMs() {
        return tickIntervalMs;
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
}