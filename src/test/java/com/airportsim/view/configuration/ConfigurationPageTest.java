package com.airportsim.view.configuration;

import static org.junit.jupiter.api.Assertions.*;

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
    private Stage stage;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        ConfigurationPage configurationPage = new ConfigurationPage(stage);
        Scene scene = new Scene(configurationPage, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Configuration Page");
        stage.show();
    }

    @Test
    public void testInitialWindowTitle() {
        assertEquals("Configuration Page", stage.getTitle());
    }

    @Test
    public void testButtonsExist() {
        assertNotNull(lookup("Start Simulation").queryButton());
        assertNotNull(lookup("Back to Menu").queryButton());
        assertNotNull(lookup("+ Add Runway").queryButton());
        assertNotNull(lookup("✕").queryButton());
    }

    @Test
    public void testSpinnersExist() {
        assertNotNull(lookup("#inboundFlowSpinner").queryAs(Spinner.class));
        assertNotNull(lookup("#outboundFlowSpinner").queryAs(Spinner.class));
        assertNotNull(lookup("#durationSpinner").queryAs(Spinner.class));
        assertNotNull(lookup("#maxWaitSpinner").queryAs(Spinner.class));
    }

    @Test
    public void testBackButton() {
        Button backButton = lookup("Back to Menu").queryButton();
        clickOn(backButton);
        assertEquals("Airport Traffic Studio", stage.getTitle());
    }

    @Test
    public void testInboundFlowSpinnerLimits() {
        Spinner<Integer> spinner = lookup("#inboundFlowSpinner").queryAs(Spinner.class);
        assertEquals(15, spinner.getValue()); // default value

        // Test max limit
        interact(() -> spinner.getValueFactory().setValue(100));
        assertEquals(100, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(101));
        assertEquals(100, spinner.getValue());

        // Test min limit
        interact(() -> spinner.getValueFactory().setValue(1));
        assertEquals(1, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(0));
        assertEquals(1, spinner.getValue());
    }

    @Test
    public void testOutboundFlowSpinnerLimits() {
        Spinner<Integer> spinner = lookup("#outboundFlowSpinner").queryAs(Spinner.class);
        assertEquals(15, spinner.getValue()); // default value

        // Test max limit
        interact(() -> spinner.getValueFactory().setValue(100));
        assertEquals(100, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(101));
        assertEquals(100, spinner.getValue());

        // Test min limit
        interact(() -> spinner.getValueFactory().setValue(1));
        assertEquals(1, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(0));
        assertEquals(1, spinner.getValue());
    }

    @Test
    public void testDurationSpinnerLimits() {
        Spinner<Integer> spinner = lookup("#durationSpinner").queryAs(Spinner.class);
        assertEquals(24, spinner.getValue()); // default value

        // Test max limit (168 hours = 7 days)
        interact(() -> spinner.getValueFactory().setValue(168));
        assertEquals(168, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(169));
        assertEquals(168, spinner.getValue());

        // Test min limit
        interact(() -> spinner.getValueFactory().setValue(1));
        assertEquals(1, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(0));
        assertEquals(1, spinner.getValue());
    }

    @Test
    public void testMaxWaitSpinnerLimits() {
        Spinner<Integer> spinner = lookup("#maxWaitSpinner").queryAs(Spinner.class);
        assertEquals(30, spinner.getValue()); // default value

        // Test max limit
        interact(() -> spinner.getValueFactory().setValue(120));
        assertEquals(120, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(121));
        assertEquals(120, spinner.getValue());

        // Test min limit
        interact(() -> spinner.getValueFactory().setValue(5));
        assertEquals(5, spinner.getValue());
        interact(() -> spinner.getValueFactory().setValue(4));
        assertEquals(5, spinner.getValue());
    }

    @Test
    public void testAddRunwayButton() {
        assertEquals(1, lookup(".runway-card").queryAll().size());
        Button addButton = lookup("+ Add Runway").queryButton();
        clickOn(addButton);

        // Check runway count increased
        assertEquals(2, lookup(".runway-card").queryAll().size());
    }

    @Test
    public void testRemoveRunwayButton() {
        // Start with 1 runway
        assertEquals(1, lookup(".runway-card").queryAll().size());

        // Try to remove the only runway (shouldnt allow it)
        Button removeButton = lookup("✕").queryButton();
        clickOn(removeButton);
        assertEquals(1, lookup(".runway-card").queryAll().size());

        // Add a second runway + remove one (should work)
        Button addButton = lookup("+ Add Runway").queryButton();
        clickOn(addButton);
        assertEquals(2, lookup(".runway-card").queryAll().size());
        Button removeButton2 = lookup("✕").queryButton();
        clickOn(removeButton2);
        assertEquals(1, lookup(".runway-card").queryAll().size());

        // Try to remove again (shouldnt work)
        Button removeButton3 = lookup("✕").queryButton();
        clickOn(removeButton3);
        assertEquals(1, lookup(".runway-card").queryAll().size());
    }

    // TODO: Change this test for when we have real logic for starting the simulation (leaving as
    // placeholder for now.)
    @Test
    public void testStartSimulationButton() {
        Button startButton = lookup("Start Simulation").queryButton();
        assertNotNull(startButton);
        clickOn(startButton);
        assertEquals("Configuration Page", stage.getTitle());
    }
}
