package com.airportsim.view.configuration;

import static org.junit.jupiter.api.Assertions.*;

import com.airportsim.view.listeners.ConfigurationListener;
import java.io.File;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class LoadScenarioPageTest extends ApplicationTest {
    private boolean backToMainMenuCalled = false;
    private boolean browseFileCalled = false;
    private File loadedScenarioFile = null;
    private LoadScenarioPage loadScenarioPage;

    @Start
    public void start(Stage stage) {
        ConfigurationListener mockListener =
                new ConfigurationListener() {
                    @Override
                    public void onBackToMainMenu() {
                        backToMainMenuCalled = true;
                    }

                    @Override
                    public void onBrowseFile(LoadScenarioPage page) {
                        browseFileCalled = true;
                        // Simulate selecting a file
                        page.setSelectedFile(new File("scenario.csv"));
                    }

                    @Override
                    public void onBrowseFiles(LoadResultsPage page) {}

                    @Override
                    public void onLoadScenario(File file) {
                        loadedScenarioFile = file;
                    }

                    @Override
                    public void onLoadResults(List<File> files) {}
                };

        loadScenarioPage = new LoadScenarioPage(mockListener);
        Scene scene = new Scene(loadScenarioPage, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testAllButtonsExist() {
        assertNotNull(lookup("Browse Files...").queryButton());
        assertNotNull(lookup("Back to Menu").queryButton());
        assertNotNull(lookup("Load Scenario").queryButton());
    }

    @Test
    public void testBackButtonCallsListener() {
        assertFalse(backToMainMenuCalled);
        clickOn(lookup("Back to Menu").queryButton());
        assertTrue(backToMainMenuCalled);
    }

    @Test
    public void testBrowseButtonCallsListener() {
        assertFalse(browseFileCalled);
        clickOn(lookup("Browse Files...").queryButton());
        assertTrue(browseFileCalled);
    }

    @Test
    public void testBrowseButtonUpdatesFileLabel() {
        clickOn(lookup("Browse Files...").queryButton());
        Label fileLabel = lookup("scenario.csv").queryAs(Label.class);
        assertNotNull(fileLabel);
        assertEquals("scenario.csv", fileLabel.getText());
    }

    @Test
    public void testSetSelectedFileUpdatesLabel() {
        interact(() -> loadScenarioPage.setSelectedFile(new File("myfile.csv")));
        Label fileLabel = lookup("myfile.csv").queryAs(Label.class);
        assertNotNull(fileLabel);
        assertEquals("myfile.csv", fileLabel.getText());
    }

    @Test
    public void testLoadScenarioButtonCallsListenerAfterFileSelected() {
        // Select a file first
        clickOn(lookup("Browse Files...").queryButton());
        // Now click Load Scenario
        clickOn(lookup("Load Scenario").queryButton());
        assertNotNull(loadedScenarioFile);
        assertEquals("scenario.csv", loadedScenarioFile.getName());
    }

    @Test
    public void testLoadScenarioButtonDoesNothingWithoutFile() {
        clickOn(lookup("Load Scenario").queryButton());
        assertNull(loadedScenarioFile);
    }

    @Test
    public void testNoFileSelectedLabelShownInitially() {
        Label noFileLabel = lookup("No file selected").queryAs(Label.class);
        assertNotNull(noFileLabel);
    }

    @Test
    public void testDropZoneExists() {
        assertNotNull(lookup(".file-drop-zone").query());
    }
}
