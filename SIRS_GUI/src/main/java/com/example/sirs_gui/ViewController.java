package com.example.sirs_gui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.gson.*;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ViewController {

    @FXML
    private TableView<Patient> table = new TableView<>();

    @FXML
    private TableColumn nif = new TableColumn("nif");

    @FXML
    private TableColumn name = new TableColumn("name");

    @FXML
    private TableColumn age = new TableColumn("age");

    private String token;

    private String role;

    private String username;

    public ViewController() {

    }

    public void passParameters(String token, String text, String username) {
        this.token = token;
        this.role = text;
        this.username=username;
    }

    public void getParams(String url) throws Exception {
        SendHttpRequest request = new SendHttpRequest();
        byte[] response = request.sendRequestWithToken("GET", null, token, url);
        JSONObject object= new JSONObject(new String(response));

        table.getColumns().clear();
        nif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        table.getColumns().addAll(nif,name,age);

        Iterator<String> keys= object.keys();
        while(keys.hasNext()){
            String keyValue=(String)keys.next();
            JSONObject objeto= object.getJSONObject(keyValue);
            String name= objeto.getString("name");
            String nif= String.valueOf(objeto.get("nif"));
            String age= String.valueOf(objeto.get("age"));
            Patient patient= new Patient(nif,name,age);
            table.getItems().add(patient);

        }

    }

    public void goBack(ActionEvent actionEvent) throws Exception{
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
            case "nurse":
            case "psa":
            case "ca":
            case "porter":
                loader = new FXMLLoader(getClass().getResource("porter_ca_nurse.fxml"));
                Parent postReg2 = loader.load();
                PorterController wController = loader.getController();
                wController.passParameters(token,role,username);
                Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene2 = new Scene(postReg2);
                stage2.setScene(scene2);
                stage2.show();
                break;
        }

    }


    public void viewDocuments(ActionEvent actionEvent) throws Exception {
        Patient patient= table.getSelectionModel().getSelectedItem();
        if (patient!=null) {
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
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Não selecionou um paciente");
            alert.setHeaderText("Selecione um paciente!");
            alert.setResizable(false);
            alert.setContentText("Selecione um paciente para ver os seus Documentos");
            alert.show();
        }

    }
}






