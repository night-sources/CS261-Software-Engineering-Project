package com.airportsim.view;

import com.airportsim.model.EmergencyStatus;
import com.airportsim.model.OperationalStatus;
import com.airportsim.model.RunwayMode;
import com.airportsim.model.SimulationEngine;
import com.airportsim.model.SimulationEvent;
import com.airportsim.model.SimulationEventListener;
import com.airportsim.viewmodel.WorldState;

public class MainController implements SimulationEventListener {
    private final SimulationEngine engine;
    private final SimulationRenderer renderer;

    public MainController(SimulationEngine engine, SimulationRenderer renderer) {
        this.engine = engine;
        this.renderer = renderer;
    }

    public void onStartClicked() {
        engine.setPaused(false);
    }

    public void onStopClicked() {
        engine.setPaused(true);
    }

    public void onPauseClicked() {
        engine.setPaused(true);
    }

    public void onResumeClicked() {
        engine.setPaused(false);
    }

    public void onRunwayOpStatusChanged(int runwayId, OperationalStatus status) {
        engine.setRunwayOpStatus(runwayId, status);
    }

    public void onRunwayModeChanged(int runwayId, RunwayMode mode) {
        engine.setRunwayMode(runwayId, mode);
    }

    public void onEmergencyDeclared(String callsign, EmergencyStatus emergency) {
        engine.setEmergencyStatus(callsign, emergency);
    }

    @Override
    public void onEvent(SimulationEvent event) {}

    public void updateView(WorldState state) {
        renderer.render(state);
    }

    public SimulationEngine getEngine() {
        return engine;
    }
}
