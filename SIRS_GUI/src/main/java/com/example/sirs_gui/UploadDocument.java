package com.example.sirs_gui;

import cipher.CipherHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UploadDocument {


    @FXML
    private Button back;

    @FXML
    private Button upload;

    @FXML
    private TextField nif;

    @FXML
    private TextField name;

    @FXML
    private TextArea observations;

    @FXML
    private TextField theme;

    @FXML
    private TextField title;




    private String token;

    private String role;

    private String username;

    public void passParameters(String token, String text,String username) {

        this.role=text;

        this.token=token;

        this.username=username;
    }


    public void uploadDocument(ActionEvent actionEvent) throws Exception {
        String nifText=nif.getText();
        String nameText=name.getText();
        String themeText=theme.getText();
        String observationsText=observations.getText();
        String titleText=title.getText();
        if (nifText.isBlank() || nameText.isBlank() || themeText.isBlank() || observationsText.isBlank() || titleText.isBlank()){
            Alert wrongCredentials=new Alert(Alert.AlertType.ERROR);
            wrongCredentials.setContentText("You have to specify the record's parameters");
            wrongCredentials.show();
        }
        else{

            JSONObject params = new JSONObject();
            params.put("nif", nifText);
            params.put("name", nameText);
            params.put("theme", themeText);
            params.put("title", titleText);
            params.put("observations", observationsText);
            params.put("username", username);

            SendHttpRequest request = new SendHttpRequest();
            byte[] response= request.sendRequestWithToken("POST", params, token, "http://localhost:9090/upload/document");

            String res= new String(response);

            if (res.contentEquals("Documento criado com sucesso")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Documento criado");
                alert.setHeaderText("O Documento foi criado");
                alert.setResizable(false);
                alert.setContentText("Clique no botão para prosseguir");

                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);

                if (button == ButtonType.OK) {
                    FXMLLoader loader = null;
                    switch (role) {
                        case "admin":
                            loader = new FXMLLoader(getClass().getResource("admin_menu_view.fxml"));
                            Parent postReg1 = loader.load();
                            AdminController controller = loader.getController();
                            controller.passParameters(token, role,username);
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(postReg1);
                            stage.setScene(scene);
                            stage.show();
                            break;
                        case "doctor":
                            loader = new FXMLLoader(getClass().getResource("doctor-menu-view.fxml"));
                            Parent postReg2 = loader.load();
                            DoctorController wController = loader.getController();
                            wController.passParameters(token, role,username);
                            Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene2 = new Scene(postReg2);
                            stage2.setScene(scene2);
                            stage2.show();
                            break;

                    }


                }
            }
            else if(res.contentEquals("Não existe nenhum paciente associado a esse NIF")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dcoumento não criado");
                alert.setHeaderText("O Documento não foi criado");
                alert.setResizable(false);
                alert.setContentText("Não existe nenhum paciente associado a esse NIF");
                alert.show();

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dcoumento não criado");
                alert.setHeaderText("O Documento não foi criado");
                alert.setResizable(false);
                alert.setContentText("O paciente com esse nome não possui esse NIF");
                alert.show();
            }
        }




    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader= null;
        switch (role){
            case "admin":
                loader = new FXMLLoader(getClass().getResource("admin_menu_view.fxml"));
                Parent postReg1 = loader.load();
                AdminController controller = loader.getController();
                controller.passParameters(token,role,username);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(postReg1);
                stage.setScene(scene);
                stage.show();
                break;
            case "doctor":
                loader = new FXMLLoader(getClass().getResource("doctor-menu-view.fxml"));
                Parent postReg2 = loader.load();
                DoctorController wController = loader.getController();
                wController.passParameters(token,role,username);
                Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene2 = new Scene(postReg2);
                stage2.setScene(scene2);
                stage2.show();
                break;
        }

    }


}
