package com.example.sirs_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WardController {
    @FXML
    private Label role;

    private String token;

    private String username;

    public void passParameters(String token, String passedRole, String username) {
        this.token=token;
        role.setText(passedRole);
        this.username=username;
    }

    public void registerPatient(ActionEvent actionEvent) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        Parent postLogin = loader.load();
        RegisterController controller = loader.getController();
        controller.passParameters(token,role.getText(),username);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();
    }
}
