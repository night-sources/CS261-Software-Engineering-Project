module com.airportsim {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;

    opens com.airportsim.view to javafx.fxml;

    exports com.airportsim;
    opens com.airportsim.viewmodel to javafx.fxml;
    opens com.airportsim.model to javafx.fxml;
}