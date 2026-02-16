package com.airportsim;

import com.airportsim.view.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class AirportSimApp extends Application {

    private MainController controller;

    @Override
    public void start(Stage stage) {
        // TODO: Composition root - wire up all dependencies
        controller = createController();

        // JavaFX UI initialisation will go here
    }

    /** Composition root: creates and wires all dependencies. */
    private MainController createController() {
        return null;
    }

    public static void main(String[] args) {
        launch();
    }
}
