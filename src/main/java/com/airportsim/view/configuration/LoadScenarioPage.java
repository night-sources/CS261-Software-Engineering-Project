package com.airportsim.view.configuration;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class LoadScenarioPage extends BorderPane {

    private Stage stage;
    private Label selectedFileLabel;
    private File selectedFile;

    public LoadScenarioPage(Stage stage) {
        this.stage = stage;
        this.getStyleClass().add("container");
        setCenter(createMainContent());
    }

    private VBox createMainContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        
        Label title = new Label("Load Scenario");
        title.getStyleClass().add("page-title");
        
        VBox uploadPanel = createUploadPanel();
        HBox buttonBar = createButtonBar();
        
        content.getChildren().addAll(title, uploadPanel, buttonBar);
        return content;
    }

    private VBox createUploadPanel() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(30));
        panel.setPrefWidth(500);
        panel.setAlignment(Pos.CENTER);
        
        Label instruction = new Label("Select a scenario configuration file (.csv)");
        instruction.getStyleClass().add("field-label");
        
        Button browseButton = new Button("Browse Files...");
        browseButton.getStyleClass().add("button-secondary");
        browseButton.setPrefWidth(200);
        browseButton.setOnAction(e -> handleBrowse());
        
        selectedFileLabel = new Label("No file selected");
        selectedFileLabel.getStyleClass().add("file-label");
        
        // build the file drop zone
        VBox dropZone = new VBox(10);
        dropZone.setAlignment(Pos.CENTER);
        dropZone.getStyleClass().add("file-drop-zone");
        dropZone.setPadding(new Insets(30));

        dropZone.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // get file when it's dropped
        dropZone.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                if (file.getName().toLowerCase().endsWith(".csv")) {
                    selectedFile = file;
                    selectedFileLabel.setText(file.getName());
                    selectedFileLabel.getStyleClass().remove("file-label");
                    selectedFileLabel.getStyleClass().add("file-label-selected");
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
        
        Label dropLabel = new Label("Drop CSV file here or click Browse");
        dropLabel.getStyleClass().add("drop-hint");
        
        dropZone.getChildren().addAll(dropLabel, browseButton, selectedFileLabel);
        
        panel.getChildren().addAll(instruction, dropZone);
        return panel;
    }

    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Scenario File");

        // only CSV files allowed
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedFile = file;
            selectedFileLabel.setText(file.getName());
            selectedFileLabel.getStyleClass().remove("file-label");
            selectedFileLabel.getStyleClass().add("file-label-selected");
        }
    }

    private HBox createButtonBar() {
        HBox buttonBar = new HBox(20);
        buttonBar.setAlignment(Pos.CENTER);
        
        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("button-quit");
        backButton.setPrefWidth(180);
        backButton.setOnAction(e -> {
            stage.setTitle("Airport Traffic Studio");
            // Go back to start menu - e.g., getScene().setRoot(new StartMenuPage(stage));
        });
        
        Button loadButton = new Button("Load Scenario");
        loadButton.getStyleClass().add("button-success");
        loadButton.setPrefWidth(180);
        loadButton.setOnAction(e -> {
            if (selectedFile != null) {
                // Do something here i guess - need to figure out what the csv will look like
                stage.setTitle("Configuration Page");
            }
        });
        
        buttonBar.getChildren().addAll(backButton, loadButton);
        return buttonBar;
    }
}