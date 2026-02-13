package com.airportsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

public class AirportSimApp extends Application {
    /** 
    * This is the main entry point for the Airport Sim, sets up the initial window with a "Start" button that leads to the config page.
    */
    @Override
    public void start(Stage stage) {
        // Start button setup
        Button startButton = new Button("Start Simulation Configuration");
        startButton.getStyleClass().add("button");
        startButton.setOnAction(event -> {
            stage.setTitle("Configuration Page"); // CHANGE WITH A STAGE.SETSCENE() CALL TO THE ACTUAL LOAD SCENARIO PAGE ONCE IT'S BUILT
        });

        // Load scenario button setup
        Button loadScenarioButton = new Button("Load Scenario");
        loadScenarioButton.getStyleClass().add("button");
        loadScenarioButton.setOnAction(event -> {
            stage.setTitle("Load Scenario Page"); // CHANGE WITH A STAGE.SETSCENE() CALL TO THE ACTUAL LOAD SCENARIO PAGE ONCE IT'S BUILT
        });

        // Load previous results button setup
        Button loadResultsButton = new Button("Load Previous Results");
        loadResultsButton.getStyleClass().add("button");
        loadResultsButton.setOnAction(event -> {
            stage.setTitle("Load Previous Results Page"); // CHANGE WITH A STAGE.SETSCENE() CALL TO THE ACTUAL LOAD SCENARIO PAGE ONCE IT'S BUILT
        });
        // Quit button setup
        Button quitButton = new Button("Quit Simulation");
        quitButton.getStyleClass().add("quit-button");
        quitButton.setOnAction(event -> {
            stage.close();
        });

        // Setting all buttons to have consistent widths
        double buttonWidth = 350;
        startButton.setPrefWidth(buttonWidth);
        loadScenarioButton.setPrefWidth(buttonWidth);
        loadResultsButton.setPrefWidth(buttonWidth);
        quitButton.setPrefWidth(buttonWidth);
  
        // Title text
        Label titleLabel = new Label("Airport Traffic Simulation");
        titleLabel.getStyleClass().add("title");
        
        // Standard Vbox for the buttons and title aligned in the middle 
        Region spacer = new Region();
        spacer.setMinHeight(100);
        VBox layout = new VBox(40, titleLabel, spacer, startButton, loadScenarioButton, loadResultsButton, quitButton);
        layout.getStyleClass().add("container");

        // Creating the actual window (1280/720) and applying the CSS
        Scene entryScene = new Scene(layout, 1280, 720);
        entryScene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm()); // apply css

        // Showing the actual window
        stage.setScene(entryScene);
        stage.setTitle("Entry Page");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
