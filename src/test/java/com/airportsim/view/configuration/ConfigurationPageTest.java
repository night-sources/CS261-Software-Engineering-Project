package com.airportsim.view.configuration;

import static org.junit.jupiter.api.Assertions.*;

import com.airportsim.view.listeners.ConfigurationListener;
import java.io.File;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class ConfigurationPageTest extends ApplicationTest {
    private boolean backToMainMenuCalled = false;

    @Start
    public void start(Stage stage) {
        ConfigurationListener mockListener =
                new ConfigurationListener() {
                    @Override
                    public void onBackToMainMenu() {
                        backToMainMenuCalled = true;
                    }

                    @Override
                    public void onBrowseFile(LoadScenarioPage page) {}

                    @Override
                    public void onBrowseFiles(LoadResultsPage page) {}

                    @Override
                    public void onLoadScenario(File file) {}

                    @Override
                    public void onLoadResults(List<File> files) {}
                };

        ConfigurationPage configurationPage = new ConfigurationPage(mockListener);
        Scene scene = new Scene(configurationPage, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testAllButtonsExist() {
        assertNotNull(lookup("Start Simulation").queryButton());
        assertNotNull(lookup("Back to Menu").queryButton());
        assertNotNull(lookup("+ Add Runway").queryButton());
        assertNotNull(lookup("✕").queryButton());
    }

    @Test
    public void testAllSpinnersExist() {
        assertNotNull(lookup("#inboundFlowSpinner").queryAs(Spinner.class));
        assertNotNull(lookup("#outboundFlowSpinner").queryAs(Spinner.class));
        assertNotNull(lookup("#durationSpinner").queryAs(Spinner.class));
        assertNotNull(lookup("#maxWaitSpinner").queryAs(Spinner.class));
    }

    @Test
    public void testBackButtonCallsListener() {
        assertFalse(backToMainMenuCalled);
        clickOn(lookup("Back to Menu").queryButton());
        assertTrue(backToMainMenuCalled);
    }

    @Test
    public void testInboundFlowSpinnerDefaultAndLimits() {
        Spinner<Integer> spinner = lookup("#inboundFlowSpinner").queryAs(Spinner.class);
        assertEquals(15, spinner.getValue());

        interact(() -> spinner.getValueFactory().setValue(100));
        assertEquals(100, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(101));
        assertEquals(100, spinner.getValue());

        interact(() -> spinner.getValueFactory().setValue(1));
        assertEquals(1, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(0));
        assertEquals(1, spinner.getValue());
    }

    @Test
    public void testOutboundFlowSpinnerDefaultAndLimits() {
        Spinner<Integer> spinner = lookup("#outboundFlowSpinner").queryAs(Spinner.class);
        assertEquals(15, spinner.getValue());

        interact(() -> spinner.getValueFactory().setValue(100));
        assertEquals(100, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(101));
        assertEquals(100, spinner.getValue());

        interact(() -> spinner.getValueFactory().setValue(1));
        assertEquals(1, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(0));
        assertEquals(1, spinner.getValue());
    }

    @Test
    public void testDurationSpinnerDefaultAndLimits() {
        Spinner<Integer> spinner = lookup("#durationSpinner").queryAs(Spinner.class);
        assertEquals(24, spinner.getValue());

        interact(() -> spinner.getValueFactory().setValue(168));
        assertEquals(168, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(169));
        assertEquals(168, spinner.getValue());

        interact(() -> spinner.getValueFactory().setValue(1));
        assertEquals(1, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(0));
        assertEquals(1, spinner.getValue());
    }

    @Test
    public void testMaxWaitSpinnerDefaultAndLimits() {
        Spinner<Integer> spinner = lookup("#maxWaitSpinner").queryAs(Spinner.class);
        assertEquals(30, spinner.getValue());

        interact(() -> spinner.getValueFactory().setValue(120));
        assertEquals(120, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(121));
        assertEquals(120, spinner.getValue());

        interact(() -> spinner.getValueFactory().setValue(5));
        assertEquals(5, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(4));
        assertEquals(5, spinner.getValue());
    }

    @Test
    public void testAddRunwayButton() {
        assertEquals(1, lookup(".runway-card").queryAll().size());
        clickOn(lookup("+ Add Runway").queryButton());
        assertEquals(2, lookup(".runway-card").queryAll().size());
    }

    @Test
    public void testRemoveRunwayButton() {
        assertEquals(1, lookup(".runway-card").queryAll().size());

        // Cannot remove the only runway
        clickOn(lookup("✕").queryButton());
        assertEquals(1, lookup(".runway-card").queryAll().size());

        // Add a second runway then remove one
        clickOn(lookup("+ Add Runway").queryButton());
        assertEquals(2, lookup(".runway-card").queryAll().size());
        clickOn(lookup("✕").queryButton());
        assertEquals(1, lookup(".runway-card").queryAll().size());

        // Cannot remove the last runway
        clickOn(lookup("✕").queryButton());
        assertEquals(1, lookup(".runway-card").queryAll().size());
    }

    @Test
    public void testStartSimulationButtonExists() {
        assertNotNull(lookup("Start Simulation").queryButton());
    }
}
