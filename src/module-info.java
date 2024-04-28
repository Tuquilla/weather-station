module src {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.fazecast.jSerialComm;
    requires com.pi4j.plugin.linuxfs;
    requires com.pi4j;
    opens data to com.pi4j;
    exports gui;
}
