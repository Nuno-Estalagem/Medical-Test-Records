package com.example.sirs_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class PartnerLabController {
    @FXML
    private Label role;

    private String token;

    private String username;

    public PartnerLabController(){

    }

    public void passParameters(String token, String passedRole, String username) {
        this.token=token;
        role.setText(passedRole);
        this.username=username;
    }

    public void uploadDocuments(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("uploadDocumentsPL.fxml"));
        Parent postLogin = loader.load();
        UploadDocumentPL controller = loader.getController();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        controller.passParameters(token,role.getText(),username);
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();
    }
}
