package com.airportsim;

import com.airportsim.model.AircraftManager;
import com.airportsim.model.EmergencyTimeComparator;
import com.airportsim.model.HoldingPattern;
import com.airportsim.model.RunwayManager;
import com.airportsim.model.SimulationEngine;
import com.airportsim.model.StatisticsManager;
import com.airportsim.model.TakeoffQueue;
import com.airportsim.view.MainController;
import com.airportsim.view.SimulationRenderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AirportSimApp extends Application {
    /**
     * This is the main entry point for the Airport Sim, sets up the initial main menu start page
     * with buttons to navigate to the various configuration and load pages.
     *
     * @param stage The stage for the JavaFX application (the main window)
     */
    private MainController controller;

    @Override
    public void start(Stage stage) {
        // Needed for aircraft manager
        EmergencyTimeComparator comparator = new EmergencyTimeComparator();
        HoldingPattern holdingPattern = new HoldingPattern(comparator);
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        // Needed for simulation engine
        RunwayManager runwayManager = new RunwayManager();
        StatisticsManager statisticsManager = new StatisticsManager();
        AircraftManager aircraftManager =
                new AircraftManager(runwayManager, holdingPattern, takeoffQueue);

        SimulationEngine engine =
                new SimulationEngine(aircraftManager, runwayManager, statisticsManager);
        SimulationRenderer renderer = new SimulationRenderer();

        controller = new MainController(engine, renderer, stage);
        Scene scene = new Scene(controller.getRootPane(), 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Airport Traffic Studio");
        stage.show();
    }

    /** Composition root: creates and wires all dependencies. */
    private MainController createController() {
        return null;
    }

    /**
     * Main method that launches the application, calling the start method above.
     *
     * @param args Command line args (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}
