package com.airportsim.view;

import com.airportsim.model.EmergencyStatus;
import com.airportsim.model.OperationalStatus;
import com.airportsim.model.RunwayMode;
import com.airportsim.model.SimulationEngine;
import com.airportsim.model.SimulationEvent;
import com.airportsim.model.SimulationEventListener;
import com.airportsim.viewmodel.WorldState;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class MainController implements SimulationEventListener {
    private final SimulationEngine engine;
    private final SimulationRenderer renderer;
    private final MainMenuPage mainMenu;
    private final Stage stage;


    public MainController(SimulationEngine engine, SimulationRenderer renderer, Stage stage) {
        this.engine = engine;
        this.renderer = renderer;
        this.stage =  stage;
        this.mainMenu = new MainMenuPage(this);
    }

    public Pane getRootPane(){
        return mainMenu;
    }


    public void onStartClicked() {
        engine.setPaused(false);
    }

    public void onStopClicked() {
        engine.setPaused(true);
    }

    public void onQuitClicked() {
        // Stop simulation and close app
        onStopClicked();
        stage.close();
    }

    public void onLoadScenarioClicked() {
        // TODO: implement loading scenario page
        System.out.println("Load Scenario button clicked");
    }

    public void onLoadResultsClicked() {
        // TODO: implement loading results page
        System.out.println("Load Results button clicked");
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
