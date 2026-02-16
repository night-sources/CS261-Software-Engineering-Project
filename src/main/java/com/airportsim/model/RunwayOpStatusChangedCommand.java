package com.airportsim.model;

public record RunwayOpStatusChangedCommand(int runwayId, OperationalStatus status) implements SimulationCommand {
    @Override
    public void execute(SimulationEngine engine) {
        engine.getRunwayManager().setRunwayStatus(runwayId, status);
    }
}
