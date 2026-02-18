package com.airportsim.view;

import static org.junit.jupiter.api.Assertions.*;

import com.airportsim.view.configuration.ConfigurationPage;
import com.airportsim.view.configuration.LoadResultsPage;
import com.airportsim.view.configuration.LoadScenarioPage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class MainMenuPageTest extends ApplicationTest {
    private Stage stage;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        MainMenuPage mainMenu = new MainMenuPage(stage);
        Scene scene = new Scene(mainMenu, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Airport Traffic Studio");
        stage.show();
    }

    @Test
    public void testInitialWindowTitle() {
        assertEquals("Airport Traffic Studio", stage.getTitle());
    }

    @Test
    public void testButtonsExist() {
        assertNotNull(lookup("Start New Scenario").queryButton());
        assertNotNull(lookup("Load Previous Scenario").queryButton());
        assertNotNull(lookup("Load Previous Results").queryButton());
        assertNotNull(lookup("Quit Simulation").queryButton());
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

    @Test
    public void testQuitButtonClosesStage() {
        Button quitButton = lookup("Quit Simulation").queryButton();
        clickOn(quitButton);
        assertFalse(stage.isShowing());
    }

    @Test
    public void testStartButtonChangesToConfigurationPage() {
        Button startButton = lookup("Start New Scenario").queryButton();
        clickOn(startButton);
        assertTrue(stage.getScene().getRoot() instanceof ConfigurationPage);
    }

    @Test
    public void testLoadScenarioButtonChangesToLoadScenarioPage() {
        Button loadScenarioButton = lookup("Load Previous Scenario").queryButton();
        clickOn(loadScenarioButton);
        assertTrue(stage.getScene().getRoot() instanceof LoadScenarioPage);
    }

    @Test
    public void testLoadResultsButtonChangesToLoadResultsPage() {
        Button loadResultsButton = lookup("Load Previous Results").queryButton();
        clickOn(loadResultsButton);
        assertTrue(stage.getScene().getRoot() instanceof LoadResultsPage);
    }
}
