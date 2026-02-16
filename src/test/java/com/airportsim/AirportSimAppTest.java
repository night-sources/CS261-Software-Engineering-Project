package com.airportsim;

import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class AirportSimAppTest extends ApplicationTest {
    private Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new AirportSimApp().start(stage);
    }

    @Test
    public void testInitialWindowTitle() {
        assertEquals("Entry Page", stage.getTitle());
    }

    @Test
    public void testButtonsExist() {
        Button startButton = lookup("Start New Scenario").queryButton();
        Button loadScenarioButton = lookup("Load Previous Scenario").queryButton();
        Button loadResultsButton = lookup("Load Previous Results").queryButton();
        Button quitButton = lookup("Quit Simulation").queryButton();

        assertNotNull(startButton);
        assertNotNull(loadScenarioButton);
        assertNotNull(loadResultsButton);
        assertNotNull(quitButton);
    }

    @Test
    public void testTitleLabelExists() {
        Label titleLabel = lookup("Airport Traffic Studio").queryAs(Label.class);
        assertNotNull(titleLabel);
    }

    @Test
    public void testStartButtonChangesTitle() {
        Button startButton = lookup("Start New Scenario").queryButton();
        clickOn(startButton);
        assertEquals("Configuration Page", stage.getTitle());
    }

    @Test
    public void testLoadScenarioButtonChangesTitle() {
        Button loadScenarioButton = lookup("Load Previous Scenario").queryButton();
        clickOn(loadScenarioButton);
        assertEquals("Load Scenario Page", stage.getTitle());
    }

    @Test
    public void testLoadResultsButtonChangesTitle() {
        Button loadResultsButton = lookup("Load Previous Results").queryButton();
        clickOn(loadResultsButton);
        assertEquals("Load Previous Results Page", stage.getTitle());
    }
}