module com.example.apitest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.net.http; // Needed for HTTP requests
    requires json.simple;  // JSON parsing library

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.apitest to javafx.fxml;
    exports com.example.apitest;
}