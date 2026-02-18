package com.airportsim;

import com.airportsim.view.MainMenuPage;
import com.airportsim.view.MainController;
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
    @Override
    public void start(Stage stage) {
        MainMenuPage mainMenu = new MainMenuPage(stage);
        Scene entryScene = new Scene(mainMenu, 1280, 720);
        entryScene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(entryScene);
        stage.setTitle("Airport Traffic Studio");
        stage.show();

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

    /**
     * Main method that launches the application, calling the start method above.
     *
     * @param args Command line args (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}
