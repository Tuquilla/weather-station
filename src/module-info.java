module src {
    requires javafx.fxml;
    requires javafx.controls;
    requires sme;
    requires com.pi4j;
    opens data to sme;
    exports gui;
}