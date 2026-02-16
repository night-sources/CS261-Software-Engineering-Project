package com.airportsim.view.configuration;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationPage extends BorderPane {

    private Stage stage;
    
    private Spinner<Integer> inboundFlowSpinner;
    private Spinner<Integer> outboundFlowSpinner;
    private Spinner<Integer> durationSpinner;
    private Spinner<Integer> maxWaitSpinner;
    private ComboBox<String> simTypeCombo;
    
    private VBox runwayListContainer;
    private List<RunwayConfig> runways = new ArrayList<>();
    
    public ConfigurationPage(Stage stage) {
        this.stage = stage;
        this.getStyleClass().add("container");
        setCenter(createMainContent());
    }
    
    private VBox createMainContent() {
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        
        // Page title
        Label title = new Label("Scenario Configuration");
        title.getStyleClass().add("page-title");
        
        // Parameters & Runways panels
        HBox panels = new HBox(30);
        panels.setAlignment(Pos.CENTER);
        panels.getChildren().addAll(createParametersPanel(), createRunwayPanel());
        
        // Back & Start buttons
        HBox buttonBar = createButtonBar();
        
        content.getChildren().addAll(title, panels, buttonBar);
        return content;
    }
    
    private VBox createParametersPanel() {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(350);
        
        Label header = new Label("Simulation Parameters");
        header.getStyleClass().add("section-header");
        
        // Flow rate spinners in the same row
        inboundFlowSpinner = createSpinner(1, 100, 15, 1);
        inboundFlowSpinner.setPrefWidth(90);
        outboundFlowSpinner = createSpinner(1, 100, 15, 1);
        outboundFlowSpinner.setPrefWidth(90);
        HBox flowRow = new HBox(20);
        flowRow.getChildren().addAll(
            createField("Inbound Flights / Hour", inboundFlowSpinner),
            createField("Outbound Flights / Hour", outboundFlowSpinner)
        );
        
        durationSpinner = createSpinner(1, 168, 24, 1);
        VBox durationField = createField("Simulation Duration (hours)", durationSpinner);
        
        maxWaitSpinner = createSpinner(5, 120, 30, 1);
        VBox waitField = createField("Max Wait Before Cancel (mins)", maxWaitSpinner);
        
        simTypeCombo = new ComboBox<>();
        simTypeCombo.getItems().addAll("Batch", "Real-time");
        simTypeCombo.setValue("Batch");
        simTypeCombo.setPrefWidth(200);
        simTypeCombo.getStyleClass().add("combo-box-custom");
        VBox typeField = createField("Simulation Type", simTypeCombo);

        // Should probably add another spinner for number of runs which appears if batch mode is selected
        
        panel.getChildren().addAll(header, flowRow, durationField, waitField, typeField);
        return panel;
    }
    
    private VBox createRunwayPanel() {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(450);
        
        Label header = new Label("Runway Configuration");
        header.getStyleClass().add("section-header");
        
        runwayListContainer = new VBox(12);
        runwayListContainer.setAlignment(Pos.TOP_CENTER);
        
        ScrollPane runwayScroll = new ScrollPane(runwayListContainer);
        runwayScroll.setFitToWidth(true);
        runwayScroll.setPrefHeight(260);
        runwayScroll.setStyle("-fx-background: #0d3b66; -fx-background-color: #0d3b66;");
        
        Button addRunwayBtn = new Button("+ Add Runway");
        addRunwayBtn.getStyleClass().add("button-secondary");
        addRunwayBtn.setOnAction(e -> addRunway());
        
        addRunway();
        
        panel.getChildren().addAll(header, runwayScroll, addRunwayBtn);
        return panel;
    }
    
    private void addRunway() {
        if (runways.size() >= 10) return;
        
        int runwayNum = runways.size() + 1;
        RunwayConfig config = new RunwayConfig(runwayNum);
        runways.add(config);
        
        VBox runwayBox = new VBox(8);
        runwayBox.getStyleClass().add("runway-card");
        runwayBox.setPadding(new Insets(12));
        
        // header with runway label and remove button
        HBox headerRow = new HBox(10);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        Label runwayLabel = new Label("Runway " + runwayNum);
        runwayLabel.getStyleClass().add("runway-label");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button removeBtn = new Button("✕");
        removeBtn.getStyleClass().add("button-remove");
        removeBtn.setOnAction(e -> removeRunway(runwayBox, config));
        headerRow.getChildren().addAll(runwayLabel, spacer, removeBtn);
        
        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER_LEFT);
        
        ComboBox<String> modeCombo = new ComboBox<>();
        modeCombo.getItems().addAll("Mixed", "Landing Only", "Takeoff Only");
        modeCombo.setValue("Mixed");
        modeCombo.getStyleClass().add("combo-box-custom");
        modeCombo.valueProperty().addListener((obs, old, val) -> config.mode = val);
        VBox modeField = createRunwayField("Mode", modeCombo);
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Available", "Runway Inspection", "Snow Clearance", "Equipment Failure");
        statusCombo.setValue("Available");
        statusCombo.getStyleClass().add("combo-box-custom");
        statusCombo.valueProperty().addListener((obs, old, val) -> config.status = val);
        VBox statusField = createRunwayField("Status", statusCombo);
        
        Spinner<Integer> lengthSpinner = createSpinner(1500, 5000, 3000, 50);
        lengthSpinner.setPrefWidth(100);
        lengthSpinner.valueProperty().addListener((obs, old, val) -> config.length = val);
        VBox lengthField = createRunwayField("Length (m)", lengthSpinner);
        
        controls.getChildren().addAll(modeField, statusField, lengthField);
        runwayBox.getChildren().addAll(headerRow, controls);
        runwayListContainer.getChildren().add(runwayBox);
    }

    private void removeRunway(VBox runwayBox, RunwayConfig config) {
        if (runways.size() <= 1) return; // min. 1 runway
        
        runways.remove(config);
        runwayListContainer.getChildren().remove(runwayBox);
        renumberRunways();
    }

    private void renumberRunways() {
        // keep the numbers in order
        for (int i = 0; i < runwayListContainer.getChildren().size(); i++) {
            VBox box = (VBox) runwayListContainer.getChildren().get(i);
            HBox header = (HBox) box.getChildren().get(0);
            Label label = (Label) header.getChildren().get(0);
            label.setText("Runway " + (i + 1));
            runways.get(i).number = i + 1;
        }
    }

    private VBox createField(String labelText, Control control) {
        VBox field = new VBox(6);
        Label label = new Label(labelText);
        label.getStyleClass().add("field-label");
        field.getChildren().addAll(label, control);
        return field;
    }

    private VBox createRunwayField(String labelText, Control control) {
            VBox field = new VBox(4);
            Label label = new Label(labelText);
            label.getStyleClass().add("field-label");
            label.setStyle("-fx-font-size: 11px;");
            field.getChildren().addAll(label, control);
            return field;
        }

    private Spinner<Integer> createSpinner(int min, int max, int defaultVal, int step) {
        Spinner<Integer> spinner = new Spinner<>(min, max, defaultVal, step);
        spinner.setEditable(true);
        spinner.setPrefWidth(200);
        spinner.getStyleClass().add("spinner-custom");
        return spinner;
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
        
        Button startButton = new Button("Start Simulation");
        startButton.getStyleClass().add("button-success");
        startButton.setPrefWidth(180);
        startButton.setOnAction(e -> {
            SimulationConfig config = buildConfig();
            
            // FOR NOW - PRINT DATA, ULTIMATELY THIS WILL START SIM ENGINE
            System.out.println("=== Simulation Configuration ===");
            System.out.println("Inbound Flow: " + config.inboundFlow + " flights/hour");
            System.out.println("Outbound Flow: " + config.outboundFlow + " flights/hour");
            System.out.println("Duration: " + config.durationHours + " hours");
            System.out.println("Max Wait Time: " + config.maxWaitMinutes + " minutes");
            System.out.println("Simulation Type: " + config.simType);
            System.out.println("\n=== Runways ===");
            for (RunwayConfig runway : config.runways) {
                System.out.println("Runway " + runway.number + ": " + runway.mode + ", " + runway.status + ", " + runway.length + "m");
            }
            System.out.println("================================");
        });
        
        buttonBar.getChildren().addAll(backButton, startButton);
        return buttonBar;
    }
    
    private SimulationConfig buildConfig() {
        SimulationConfig config = new SimulationConfig();
        config.inboundFlow = inboundFlowSpinner.getValue();
        config.outboundFlow = outboundFlowSpinner.getValue();
        config.durationHours = durationSpinner.getValue();
        config.maxWaitMinutes = maxWaitSpinner.getValue();
        config.simType = simTypeCombo.getValue();
        config.runways = new ArrayList<>(runways);
        return config;
    }
    
    public static class RunwayConfig {
        public int number;
        public String mode = "Mixed";
        public String status = "Available";
        public int length = 3000;
        
        public RunwayConfig(int number) {
            this.number = number;
        }
    }
    /* I know there's a SimulationConfig class in /model, but for 
    now just using this because that one hasn't been implemented 
    and it's also a bit strange (why's there a file for runways?) */
    public static class SimulationConfig {
        public int inboundFlow;
        public int outboundFlow;
        public int durationHours;
        public int maxWaitMinutes;
        public String simType;
        public List<RunwayConfig> runways;
    }
}