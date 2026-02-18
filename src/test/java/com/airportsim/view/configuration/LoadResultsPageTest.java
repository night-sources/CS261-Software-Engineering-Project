package com.airportsim.view.configuration;

import static org.junit.jupiter.api.Assertions.*;

import com.airportsim.view.listeners.ConfigurationListener;
import java.io.File;
import java.util.Arrays;
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
public class LoadResultsPageTest extends ApplicationTest {
    private boolean backToMainMenuCalled = false;
    private boolean browseFilesCalled = false;
    private List<File> loadedResultFiles = null;
    private LoadResultsPage loadResultsPage;

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
                    public void onBrowseFiles(LoadResultsPage page) {
                        browseFilesCalled = true;
                        // Simulate selecting files
                        page.setSelectedFiles(
                                Arrays.asList(new File("result1.csv"), new File("result2.csv")));
                    }

                    @Override
                    public void onLoadScenario(File file) {}

                    @Override
                    public void onLoadResults(List<File> files) {
                        loadedResultFiles = files;
                    }
                };

        loadResultsPage = new LoadResultsPage(mockListener);
        Scene scene = new Scene(loadResultsPage, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testAllButtonsExist() {
        assertNotNull(lookup("Browse Files...").queryButton());
        assertNotNull(lookup("Back to Menu").queryButton());
        assertNotNull(lookup("View Results").queryButton());
    }

    @Test
    public void testBackButtonCallsListener() {
        assertFalse(backToMainMenuCalled);
        clickOn(lookup("Back to Menu").queryButton());
        assertTrue(backToMainMenuCalled);
    }

    @Test
    public void testBrowseButtonCallsListener() {
        assertFalse(browseFilesCalled);
        clickOn(lookup("Browse Files...").queryButton());
        assertTrue(browseFilesCalled);
    }

    @Test
    public void testBrowseButtonUpdatesFileList() {
        clickOn(lookup("Browse Files...").queryButton());
        // After browsing, file names should appear
        assertNotNull(lookup("result1.csv").queryAs(Label.class));
        assertNotNull(lookup("result2.csv").queryAs(Label.class));
    }

    @Test
    public void testNoFilesSelectedLabelShownInitially() {
        Label noFilesLabel = lookup("No files selected").queryAs(Label.class);
        assertNotNull(noFilesLabel);
    }

    @Test
    public void testViewResultsButtonCallsListenerAfterFilesSelected() {
        // Select files first
        clickOn(lookup("Browse Files...").queryButton());
        // Now click View Results
        clickOn(lookup("View Results").queryButton());
        assertNotNull(loadedResultFiles);
        assertEquals(2, loadedResultFiles.size());
    }

    @Test
    public void testViewResultsButtonDoesNothingWithoutFiles() {
        clickOn(lookup("View Results").queryButton());
        assertNull(loadedResultFiles);
    }

    @Test
    public void testRemoveFileButton() {
        // Add files first
        clickOn(lookup("Browse Files...").queryButton());
        // Should have 2 file rows with remove buttons
        assertEquals(2, lookup(".file-row").queryAll().size());
        // Click the first remove button
        clickOn(lookup("✕").queryButton());
        assertEquals(1, lookup(".file-row").queryAll().size());
    }

    @Test
    public void testDropZoneExists() {
        assertNotNull(lookup(".file-drop-zone").query());
    }
}
