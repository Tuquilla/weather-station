module gui {
    requires javafx.fxml;
    requires javafx.controls;
    opens gui to javafx.graphics;
    exports gui;
}