package com.airportsim.view.configuration;

import com.airportsim.view.listeners.ConfigurationListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Creates the page where users can load previous sim results from CSV files. Just shows a file
 * selector.
 *
 * <p>TODO: Once a file is selected, parse it with the previous results stats on display.
 */
public class LoadResultsPage extends BorderPane {
    private VBox fileListContainer;
    private List<File> selectedFiles = new ArrayList<>();
    private final ConfigurationListener listener;

    public LoadResultsPage(ConfigurationListener listener) {
        this.listener = listener;
        this.getStyleClass().add("container");
        setCenter(createMainContent());
    }

    private VBox createMainContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        Label title = new Label("Load Results");
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
        panel.setPrefWidth(550);
        panel.setAlignment(Pos.CENTER);

        Label instruction = new Label("Select result file(s) to load (.csv)");
        instruction.getStyleClass().add("field-label");

        // build the file drop zone
        VBox dropZone = new VBox(15);
        dropZone.setAlignment(Pos.CENTER);
        dropZone.getStyleClass().add("file-drop-zone");
        dropZone.setPadding(new Insets(25));

        dropZone.setOnDragOver(
                event -> {
                    if (event.getDragboard().hasFiles()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    }
                    event.consume();
                });

        // get files when dropped
        dropZone.setOnDragDropped(
                event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasFiles()) {
                        List<File> csvFiles =
                                db.getFiles().stream()
                                        .filter(f -> f.getName().toLowerCase().endsWith(".csv"))
                                        .toList();

                        if (!csvFiles.isEmpty()) {
                            selectedFiles.clear();
                            selectedFiles.addAll(csvFiles);
                            fileListContainer.getChildren().clear();
                            for (File file : csvFiles) {
                                fileListContainer.getChildren().add(createFileRow(file));
                            }
                            success = true;
                        }
                    }
                    event.setDropCompleted(success);
                    event.consume();
                });

        Label dropLabel = new Label("Drop CSV files here or click Browse");
        dropLabel.getStyleClass().add("drop-hint");

        Button browseButton = new Button("Browse Files...");
        browseButton.getStyleClass().add("button-secondary");
        browseButton.setPrefWidth(200);
        browseButton.setOnAction(e -> listener.onBrowseFiles(this));

        dropZone.getChildren().addAll(dropLabel, browseButton);

        fileListContainer = new VBox(8);
        fileListContainer.setAlignment(Pos.CENTER_LEFT);

        Label noFilesLabel = new Label("No files selected");
        noFilesLabel.getStyleClass().add("file-label");
        fileListContainer.getChildren().add(noFilesLabel);

        panel.getChildren().addAll(instruction, dropZone, fileListContainer);
        return panel;
    }

    public void setSelectedFiles(List<File> files) {
        if (files != null && !files.isEmpty()) {
            selectedFiles.clear();
            selectedFiles.addAll(files);
            fileListContainer.getChildren().clear();

            for (File file : files) {
                fileListContainer.getChildren().add(createFileRow(file));
            }
        }
    }

    private HBox createFileRow(File file) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("file-row");
        row.setPadding(new Insets(8, 12, 8, 12));

        Label fileName = new Label(file.getName());
        fileName.getStyleClass().add("file-label-selected");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeBtn = new Button("✕");
        removeBtn.getStyleClass().add("button-remove");
        removeBtn.setOnAction(
                e -> {
                    selectedFiles.remove(file);
                    fileListContainer.getChildren().remove(row);
                    if (fileListContainer.getChildren().isEmpty()) {
                        Label noFilesLabel = new Label("No files selected");
                        noFilesLabel.getStyleClass().add("file-label");
                        fileListContainer.getChildren().add(noFilesLabel);
                    }
                });

        row.getChildren().addAll(fileName, spacer, removeBtn);
        return row;
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

        Button loadButton = new Button("View Results");
        loadButton.getStyleClass().add("button-success");
        loadButton.setPrefWidth(180);
        loadButton.setOnAction(
                e -> {
                    if (!selectedFiles.isEmpty()) {
                        listener.onLoadResults(selectedFiles);
                    }
                });

        buttonBar.getChildren().addAll(backButton, loadButton);
        return buttonBar;
    }
}
