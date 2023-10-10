module com.example.sirs_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.auth0.jwt;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;
    requires com.google.gson;
    opens com.example.sirs_gui to javafx.fxml;
    exports com.example.sirs_gui;


}