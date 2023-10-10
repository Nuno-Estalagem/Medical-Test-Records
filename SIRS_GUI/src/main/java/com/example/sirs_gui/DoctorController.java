package com.example.sirs_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DoctorController {
    @FXML
    private Label role;

    private String token;

    private Button button;

    private String username;

    public DoctorController(){

    }

    public void passParameters(String token, String passedRole, String username) {
        this.token=token;
        role.setText(passedRole);
        this.username=username;
    }

    public void assignPatient(ActionEvent actionEvent) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assignPatientsDoctor.fxml"));
        Parent postLogin = loader.load();
        DoctorAssignController controller = loader.getController();
        controller.passParameters(token,role.getText());
        controller.getParams("http://localhost:9090/view/patients");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();
    }

    public void uploadDocuments(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("uploadDocuments.fxml"));
        Parent postLogin = loader.load();
        UploadDocument controller = loader.getController();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        controller.passParameters(token,role.getText(),username);
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();
    }


    public void viewPatients(ActionEvent actionEvent) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DoctorviewPatients.fxml"));
        Parent postLogin = loader.load();
        DoctorViewController controller = loader.getController();
        controller.passParameters(token,role.getText(),username);
        controller.getParams("http://localhost:9090/doctor/assignedPatients");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();
    }


}
