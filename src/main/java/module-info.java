module com.airportsim {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.airportsim.view to javafx.fxml;

    exports com.airportsim;
}