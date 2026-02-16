package com.airportsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class AirportSimApp extends Application {
    /** 
    * This is the main entry point for the Airport Sim, sets up the initial window with a "Start" button that leads to the config page.
    */
    @Override
    public void start(Stage stage) {
        // --- BUTTON SETUP ---
        Button startButton = new Button("Start New Scenario");
        startButton.getStyleClass().add("button");
        startButton.setOnAction(event -> {
            stage.setTitle("Configuration Page"); // CHANGE WITH A STAGE.SETSCENE() CALL TO THE ACTUAL LOAD SCENARIO PAGE ONCE IT'S BUILT
        });

        Button loadScenarioButton = new Button("Load Previous Scenario");  // Load scenario button setup
        loadScenarioButton.getStyleClass().add("button");
        loadScenarioButton.setOnAction(event -> {
            stage.setTitle("Load Scenario Page"); // CHANGE WITH A STAGE.SETSCENE() CALL TO THE ACTUAL LOAD SCENARIO PAGE ONCE IT'S BUILT
        });

        Button loadResultsButton = new Button("Load Previous Results"); // Load previous results button setup
        loadResultsButton.getStyleClass().add("button");
        loadResultsButton.setOnAction(event -> {
            stage.setTitle("Load Previous Results Page"); // CHANGE WITH A STAGE.SETSCENE() CALL TO THE ACTUAL LOAD SCENARIO PAGE ONCE IT'S BUILT
        });
        
        Button quitButton = new Button("Quit Simulation"); // Quit button setup
        quitButton.getStyleClass().add("quit-button");
        quitButton.setOnAction(event -> {
            stage.close();
        });

        double buttonWidth = 350; // Setting all buttons to have consistent widths
        startButton.setPrefWidth(buttonWidth);
        loadScenarioButton.setPrefWidth(buttonWidth);
        loadResultsButton.setPrefWidth(buttonWidth);
        quitButton.setPrefWidth(buttonWidth);
  
        // Title text
        Label titleLabel = new Label("Airport Traffic Studio");
        titleLabel.getStyleClass().add("title");
        
        // Standard Vbox for the buttons and title aligned in the middle 
        Region spacer = new Region();
        spacer.setMinHeight(60);
        VBox layout = new VBox(40, titleLabel, spacer, startButton, loadScenarioButton, loadResultsButton, quitButton);
        layout.setStyle("-fx-alignment: center; -fx-padding: 40;");

        // Creating the plane animation 
        Image planeImage = new Image(getClass().getResource("/images/plane.png").toExternalForm());
        ImageView planeView = new ImageView(planeImage);
        planeView.setFitWidth(800);
        planeView.setPreserveRatio(true);
        
        TranslateTransition transition = new TranslateTransition(Duration.seconds(10), planeView);
        transition.setFromX(-530);      // Start off-screen left
        transition.setFromY(720);       // Start off-screen bottom
        transition.setToX(470);        // End off-screen right
        transition.setToY(-600);        // End off-screen top
        transition.setCycleCount(TranslateTransition.INDEFINITE); // Loop forever
        transition.play();

        // Create a StackPane to create the layer order : plane then layout over it
        StackPane root = new StackPane();
        root.getStyleClass().add("container-main-menu");
        root.getChildren().addAll(planeView, layout);

        // Creating the actual window (1280/720) and applying the CSS
        Scene entryScene = new Scene(root, 1280, 720);
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
