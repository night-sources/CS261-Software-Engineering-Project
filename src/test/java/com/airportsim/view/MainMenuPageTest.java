package com.airportsim.view;

import static org.junit.jupiter.api.Assertions.*;

import com.airportsim.view.listeners.MainMenuListener;
import javafx.scene.Scene;
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
    private boolean startNewScenarioCalled = false;
    private boolean loadPreviousScenarioCalled = false;
    private boolean loadPreviousResultsCalled = false;
    private boolean quitCalled = false;

    @Start
    public void start(Stage stage) {
        this.stage = stage;

        MainMenuListener mockListener =
                new MainMenuListener() {
                    @Override
                    public void onStartNewScenario() {
                        startNewScenarioCalled = true;
                    }

                    @Override
                    public void onLoadPreviousScenario() {
                        loadPreviousScenarioCalled = true;
                    }

                    @Override
                    public void onLoadPreviousResults() {
                        loadPreviousResultsCalled = true;
                    }

                    @Override
                    public void onQuit() {
                        quitCalled = true;
                        stage.close();
                    }
                };

        MainMenuPage mainMenu = new MainMenuPage(mockListener);
        Scene scene = new Scene(mainMenu, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testAllButtonsExist() {
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
    public void testStartNewScenarioCallsListener() {
        assertFalse(startNewScenarioCalled);
        clickOn(lookup("Start New Scenario").queryButton());
        assertTrue(startNewScenarioCalled);
    }

    @Test
    public void testLoadPreviousScenarioCallsListener() {
        assertFalse(loadPreviousScenarioCalled);
        clickOn(lookup("Load Previous Scenario").queryButton());
        assertTrue(loadPreviousScenarioCalled);
    }

    @Test
    public void testLoadPreviousResultsCallsListener() {
        assertFalse(loadPreviousResultsCalled);
        clickOn(lookup("Load Previous Results").queryButton());
        assertTrue(loadPreviousResultsCalled);
    }

    @Test
    public void testQuitCallsListenerAndClosesStage() {
        assertFalse(quitCalled);
        clickOn(lookup("Quit Simulation").queryButton());
        assertTrue(quitCalled);
        assertFalse(stage.isShowing());
    }
}
