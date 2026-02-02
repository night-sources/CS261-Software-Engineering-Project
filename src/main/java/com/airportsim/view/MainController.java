package com.airportsim.view;

import com.airportsim.model.OperationalStatus;
import com.airportsim.model.SimulationEngine;
import com.airportsim.model.SimulationEvent;
import com.airportsim.model.SimulationEventListener;
import com.airportsim.viewmodel.WorldState;
import java.io.File;

public class MainController implements SimulationEventListener {
    private SimulationEngine engine;
    private SimulationRenderer renderer;

    public MainController() {}

    public void loadConfig(File runwayFile, File configFile) {}

    public void onStartClicked() {}

    public void onStopClicked() {}

    public void onPauseClicked() {}

    public void onResumeClicked() {}

    public void onRunwayConfigChanged(int runwayId, OperationalStatus status) {}

    @Override
    public void onEvent(SimulationEvent event) {}

    public void updateView(WorldState view) {}
}
