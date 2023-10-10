package com.example.sirs_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.List;

public class showDocController {

    private String token;

    private String role;

    private String username;

    private Document document;

    @FXML
    private Label theme;

    @FXML
    private Label title;

    @FXML
    private Label observations;

    @FXML
    private Label poster;

    private JSONObject params;

    private Patient patient;

    public void passParameters(String token, String role, String username, Document document, JSONObject params, Patient patient) {
        this.token=token;
        this.role=role;
        this.username=username;
        this.document=document;
        this.params=params;
        this.patient=patient;
    }

    public void getParams(String passedRole) {
        JSONObject documento= params.getJSONObject(document.getID());
        theme.setText(documento.getString("theme"));
        title.setText(documento.getString("title"));
        poster.setText("Posted by: " +documento.getString("uploader"));
        observations.setText(documento.getString("observations"));
    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("documentSelector.fxml"));
        Parent postLogin = loader.load();
        DocumentController controller = loader.getController();
        controller.passParameters(token, role, username, patient);
        controller.getParams("http://localhost:9090/post/documents");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();
    }
}
