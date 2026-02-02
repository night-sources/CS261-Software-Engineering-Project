package com.airportsim.model;

import com.airportsim.viewmodel.Snapshot;
import com.airportsim.viewmodel.SnapshotFactory;
import java.util.List;
import java.util.Random;

public class AircraftManager implements Tickable, SnapshotFactory {
    private int inboundTimer;
    private int outboundTimer;
    private List<Aircraft> activeAircraft;
    private Random random;

    public AircraftManager() {}

    public void spawnInbound() {}

    public void spawnOutbound() {}

    public void checkFuelLevels() {}

    @Override
    public void tick(long currentTick) {}

    @Override
    public Snapshot getSnapshot() {}
}
