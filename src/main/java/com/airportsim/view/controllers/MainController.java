package com.airportsim.view.controllers;

import com.airportsim.model.EmergencyStatus;
import com.airportsim.model.OperationalStatus;
import com.airportsim.model.RunwayMode;
import com.airportsim.model.SimulationEngine;
import com.airportsim.model.SimulationEvent;
import com.airportsim.model.SimulationEventListener;
import com.airportsim.view.MainMenuPage;
import com.airportsim.view.PageFactory;
import com.airportsim.view.SimulationRenderer;
import com.airportsim.viewmodel.WorldState;
import javafx.stage.Stage;

/** Main controller for coordinating the simulation engine with the view/user interactions. */
public class MainController implements SimulationEventListener {
    private final SimulationEngine engine;
    private final SimulationRenderer renderer;
    private final Stage stage;
    private final NavigationController navigationController;
    private final MainMenuController mainMenuController;
    private final ConfigurationController configurationController;
    private final SimulationController simulationController;

    public MainController(
            SimulationEngine engine,
            SimulationRenderer renderer,
            Stage stage,
            PageFactory pageFactory) {
        this.engine = engine;
        this.renderer = renderer;
        this.stage = stage;
        this.navigationController = new NavigationController(stage, pageFactory);
        this.mainMenuController = new MainMenuController(this, navigationController);
        this.configurationController = new ConfigurationController(this, navigationController);
        this.simulationController = new SimulationController(this, navigationController);
    }

    public MainMenuPage getRootPane() {
        return navigationController.createMainMenuPage(mainMenuController);
    }

    public void loadMainMenuPage() {
        navigationController.showMainMenu(mainMenuController);
    }

    public void onQuitClicked() {
        onPauseClicked();
        stage.close();
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

    public Stage getStage() {
        return stage;
    }

    public MainMenuController getMainMenuController() {
        return mainMenuController;
    }

    public ConfigurationController getConfigurationController() {
        return configurationController;
    }

    public SimulationController getSimulationController() {
        return simulationController;
    }

    public NavigationController getNavigationController() {
        return navigationController;
    }
}
