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
import javafx.scene.control.Alert;
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
public class DoctorAssignController {
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

    public DoctorAssignController() {

    }

    public void passParameters(String token, String text) {
        this.token = token;
        this.role = text;
    }

    public void getParams(String url) throws Exception {
        SendHttpRequest request = new SendHttpRequest();
        byte[] response = request.sendRequestWithToken("GET", null, token, url);
        if( response==null){
            Alert noneSelected=new Alert(Alert.AlertType.ERROR);
            noneSelected.setContentText("There are no Patients to Assign");
            noneSelected.show();
        }
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

    public void goBack(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-menu-view.fxml"));
        Parent postReg1 = loader.load();
        DoctorController controller = loader.getController();
        controller.passParameters(token,role,username);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(postReg1);
        stage.setScene(scene);
        stage.show();
    }

    public void assignPatient(ActionEvent actionEvent) throws Exception {
        Patient patient=table.getSelectionModel().getSelectedItem();
        if (patient!=null) {
            SendHttpRequest request = new SendHttpRequest();

            DecodedJWT decode = JWT.decode(token.replaceAll("^\"+|\"+$", ""));
            Claim claim = decode.getClaim("preferred_username");
            String doctor=claim.asString().replaceAll("^\"+|\"+$", "");

            JSONObject body = new JSONObject();
            body.put("nif", patient.getNif());
            body.put("name", patient.getName());
            body.put("age", patient.getAge());
            body.put("doctor", doctor);

            byte[] response = request.sendRequestWithToken("POST", body, token, "http://localhost:9090/doctor/assign");

            Alert assignState=new Alert(Alert.AlertType.INFORMATION);
            assignState.setContentText(new String(response));
            assignState.show();

        }

        else{
            Alert noneSelected=new Alert(Alert.AlertType.ERROR);
            noneSelected.setContentText("You have to select a patient to assign");
            noneSelected.show();
        }

    }



}
