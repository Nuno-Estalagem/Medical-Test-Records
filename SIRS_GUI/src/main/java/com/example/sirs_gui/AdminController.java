package com.example.sirs_gui;

import com.example.sirs_gui.RegisterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminController {

    @FXML
    private Label role;

    private String token;

    private String username;

    public AdminController(){
            }


    public void passParameters(String token, String passedRole, String username) {
        this.token=token;
        role.setText(passedRole);
        this.username=username;
    }

    public void viewPatients(ActionEvent actionEvent) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewPatients.fxml"));
        Parent postLogin = loader.load();
        ViewController controller = loader.getController();
        controller.passParameters(token,role.getText(),username);
        controller.getParams("http://localhost:9090/view/patients");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();
    }

    public void uploadDocuments(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("uploadDocuments.fxml"));
        Parent postLogin = loader.load();
        UploadDocument controller = loader.getController();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        controller.passParameters(token,role.getText(),username);
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();
    }

    public void registerPatient(ActionEvent actionEvent) throws Exception {
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
