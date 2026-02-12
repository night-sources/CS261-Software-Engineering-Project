package com.airportsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AirportSimApp extends Application {
    /** 
    * This is the main entry point for the Airport Sim, sets up the initial window with a "Start" button that leads to the config page.
    */
    @Override
    public void start(Stage stage) {
        // Setting up the start button
        Button startButton = new Button("Start");
        startButton.getStyleClass().add("button");
        startButton.setOnAction(event -> {
            stage.setTitle("Configuration Page");
        });
        
        // Standard Vbox for the button with padding + spacing for now (will refine later)
        VBox layout = new VBox(20, startButton);
        layout.setStyle("-fx-padding: 40; -fx-alignment: center;");
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
