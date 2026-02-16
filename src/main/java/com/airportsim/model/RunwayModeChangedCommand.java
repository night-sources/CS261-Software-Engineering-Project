package com.airportsim.model;

public record RunwayModeChangedCommand(int id, RunwayMode mode) implements SimulationCommand {
    public void execute(SimulationEngine engine) {
        engine.getRunwayManager().setRunwayMode(id, mode);
    }
}
