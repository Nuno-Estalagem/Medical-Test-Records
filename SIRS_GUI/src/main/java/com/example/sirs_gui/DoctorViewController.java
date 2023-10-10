package com.example.sirs_gui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DoctorViewController {
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

    public void passParameters(String token, String text,String username) {
        this.token = token;
        this.role = text;
        this.username=username;
    }

    public void getParams(String url) throws Exception {
        SendHttpRequest request = new SendHttpRequest();
        JSONObject body = new JSONObject();
        DecodedJWT decode = JWT.decode(token.replaceAll("^\"+|\"+$", ""));
        Claim claim = decode.getClaim("preferred_username");
        this.username=claim.asString().replaceAll("^\"+|\"+$", "");
        body.put("username", username);
        byte[] response = request.sendRequestWithToken("POST", body, token, url);
        System.out.println(new String(response)+ "here");
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-menu-view.fxml"));
        Parent postReg1 = loader.load();
        DoctorController controller = loader.getController();
        controller.passParameters(token,role,username);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(postReg1);
        stage.setScene(scene);
        stage.show();
    }


    public void viewDocuments(ActionEvent actionEvent) throws Exception {
        Patient patient= table.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("documentSelector.fxml"));
        Parent postLogin = loader.load();
        DocumentController controller = loader.getController();
        controller.passParameters(token,role,username,patient);
        controller.getParams("http://localhost:9090/post/documents");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(postLogin);
        stage.setScene(scene);
        stage.show();

    }

}



