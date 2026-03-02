package com.airportsim.view.configuration;

import com.airportsim.view.listeners.ConfigurationListener;
import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Creates the page where users can load previous scenario parameters from CSV files. Just shows a
 * file selector.
 *
 * <p>TODO: Once a file is selected, parse it with the previous scenario parameters on display, and
 * start the sim with those parameters.
 */
public class LoadScenarioPage extends BorderPane {
    private Label selectedFileLabel;
    private File selectedFile;
    private final ConfigurationListener listener;

    public LoadScenarioPage(ConfigurationListener listener) {
        this.listener = listener;
        this.getStyleClass().add("container");
        setCenter(createMainContent());
    }

    private VBox createMainContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        Label title = new Label("Load A Previous Scenario");
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
        browseButton.setOnAction(e -> listener.onBrowseFile(this));

        selectedFileLabel = new Label("No file selected");
        selectedFileLabel.getStyleClass().add("file-label");

        // build the file drop zone
        VBox dropZone = new VBox(10);
        dropZone.setAlignment(Pos.CENTER);
        dropZone.getStyleClass().add("file-drop-zone");
        dropZone.setPadding(new Insets(30));

        dropZone.setOnDragOver(
                event -> {
                    if (event.getDragboard().hasFiles()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    }
                    event.consume();
                });

        // get file when it's dropped
        dropZone.setOnDragDropped(
                event -> {
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

    public void setSelectedFile(File file) {
        if (file != null) {
            this.selectedFile = file;
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
        backButton.setOnAction(
                e -> {
                    listener.onBackToMainMenu();
                });

        Button loadButton = new Button("Load Scenario");
        loadButton.getStyleClass().add("button-success");
        loadButton.setPrefWidth(180);
        loadButton.setOnAction(
                e -> {
                    if (selectedFile != null) {
                        listener.onLoadScenario(selectedFile);
                    }
                });

        buttonBar.getChildren().addAll(backButton, loadButton);
        return buttonBar;
    }
}
