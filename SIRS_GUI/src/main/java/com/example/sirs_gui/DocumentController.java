package com.example.sirs_gui;

import cipher.CipherHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DocumentController {

    private String token;

    private String role;

    private String username;

    private Patient patient;

    @FXML
    private TableView<Document> table = new TableView<>();


    @FXML
    private TableColumn theme = new TableColumn("theme");

    @FXML
    private TableColumn lab = new TableColumn("Lab");

    @FXML
    private TableColumn id = new TableColumn("ID");

    @FXML
    private Label nif;

    @FXML
    private Label name;

    @FXML
    private Label age;

    private JSONObject documents = new JSONObject();

    public void passParameters(String token, String role, String username, Patient patient) {
        this.token = token;
        this.role = role;
        this.username = username;
        this.patient = patient;
    }

    public void getParams(String url) throws Exception {
        SendHttpRequest request = new SendHttpRequest();
        JSONObject docObject = new JSONObject();
        docObject.put("nif", patient.getNif());
        docObject.put("name", patient.getName());
        docObject.put("age", patient.getAge());

        byte[] response = request.sendRequestWithToken("POST", docObject,token, url);
        documents= new JSONObject(new String(response));
        System.out.println(documents);

        table.getColumns().clear();
        theme.setCellValueFactory(new PropertyValueFactory<>("Theme"));
        lab.setCellValueFactory(new PropertyValueFactory<>("Lab"));
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        table.getColumns().addAll(theme,lab,id);

        Iterator<String> keys= documents.keys();
        while(keys.hasNext()){
            String keyValue=keys.next();
            JSONObject objeto= documents.getJSONObject(keyValue);
            String theme= objeto.getString("title");
            String lab= String.valueOf(objeto.get("lab"));
            String id= String.valueOf(objeto.get("id"));
            Document document= new Document(theme,lab,id);
            table.getItems().add(document);
        }
        nif.setText(docObject.getString("nif"));
        name.setText(docObject.getString("name"));
        age.setText(docObject.getString("age"));

    }


    public void goBack(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = null;
        switch (role) {
            case "doctor":
                loader = new FXMLLoader(getClass().getResource("DoctorviewPatients.fxml"));
                Parent postReg1 = loader.load();
                DoctorViewController controller = loader.getController();
                controller.passParameters(token, role, username);
                controller.getParams("http://localhost:9090/doctor/assignedPatients");
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(postReg1);
                stage.setScene(scene);
                stage.show();
                break;
            default:
                loader = new FXMLLoader(getClass().getResource("viewPatients.fxml"));
                Parent postReg2 = loader.load();
                ViewController viewController = loader.getController();
                viewController.passParameters(token, role, username);
                viewController.getParams("http://localhost:9090/view/patients");
                Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene2 = new Scene(postReg2);
                stage2.setScene(scene2);
                stage2.show();
                break;
        }

    }

    public void viewDocument(ActionEvent actionEvent) throws Exception {
        Document document = table.getSelectionModel().getSelectedItem();
        if (document != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("showDocument.fxml"));
            Parent postLogin = loader.load();
            showDocController controller = loader.getController();
            controller.passParameters(token, role, username, document, documents, patient);
            controller.getParams("http://localhost:9090/view/patients");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(postLogin);
            stage.setScene(scene);
            stage.show();
        }

    }
}
