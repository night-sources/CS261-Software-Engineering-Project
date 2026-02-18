package com.airportsim.view;

import com.airportsim.view.configuration.*;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenuPage extends StackPane {

    private final MainController controller;

    public MainMenuPage(MainController controller) {

        this.controller = controller; 
        // --- BUTTON SETUP ---
        Button startButton = new Button("Start New Scenario");
        startButton.getStyleClass().add("button-success");
        startButton.setOnAction(
                event -> {
                    stage.setTitle("Configuration Page");
                    ConfigurationPage configurationPage = new ConfigurationPage(stage);
                    stage.getScene().setRoot(configurationPage);
                });

        Button loadScenarioButton = new Button("Load Previous Scenario");
        loadScenarioButton.getStyleClass().add("button-success");
        loadScenarioButton.setOnAction(
                event -> {
                    stage.setTitle("Load Scenario Page");
                    LoadScenarioPage loadScenarioPage = new LoadScenarioPage(stage);
                    stage.getScene().setRoot(loadScenarioPage);
                });

        Button loadResultsButton = new Button("Load Previous Results");
        loadResultsButton.getStyleClass().add("button-success");
        loadResultsButton.setOnAction(
                event -> {
                    stage.setTitle("Load Previous Results Page");
                    LoadResultsPage loadResultsPage = new LoadResultsPage(stage);
                    stage.getScene().setRoot(loadResultsPage);
                });

        Button quitButton = new Button("Quit Simulation");
        quitButton.getStyleClass().add("button-quit");
        quitButton.setOnAction(event -> stage.close());

        double buttonWidth = 350;
        startButton.setPrefWidth(buttonWidth);
        loadScenarioButton.setPrefWidth(buttonWidth);
        loadResultsButton.setPrefWidth(buttonWidth);
        quitButton.setPrefWidth(buttonWidth);

        // Page title
        Label titleLabel = new Label("Airport Traffic Studio");
        titleLabel.getStyleClass().add("title");

        // Layout setup with buttons, title, animation + bg, space between title and buttons
        Region spacer = new Region();
        spacer.setMinHeight(60);
        VBox layout =
                new VBox(
                        40,
                        titleLabel,
                        spacer,
                        startButton,
                        loadScenarioButton,
                        loadResultsButton,
                        quitButton);
        layout.setStyle("-fx-alignment: center; -fx-padding: 40;");

        // BG image
        Image planeImage = new Image(getClass().getResource("/images/plane.png").toExternalForm());
        ImageView planeView = new ImageView(planeImage);
        planeView.setFitWidth(800);
        planeView.setPreserveRatio(true);

        // Plane animatiion
        TranslateTransition transition = new TranslateTransition(Duration.seconds(10), planeView);
        transition.setFromX(-530); // Start off-screen left
        transition.setFromY(720); // Start off-screen bottom
        transition.setToX(470); // End off-screen right
        transition.setToY(-600); // End off-screen top
        transition.setCycleCount(TranslateTransition.INDEFINITE); // Loop forever
        transition.play();

        this.getStyleClass().add("container-main-menu");
        this.getChildren().addAll(planeView, layout);
    }
}
