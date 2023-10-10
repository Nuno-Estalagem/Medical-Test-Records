package com.example.sirs_gui;

import com.example.sirs_gui.SendHttpRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.jar.Attributes;

public class RegisterController {

    @FXML
    private TextField nif;

    @FXML
    private TextField name;

    @FXML
    private TextField age;

    private String token;

    private String role;
    private String username;

    public RegisterController(){

    }


    public void registerUser(ActionEvent actionEvent) throws Exception {
        JSONObject body = new JSONObject();
        body.put("nif", nif.getText());
        body.put("name", name.getText());
        body.put("age", age.getText());
        SendHttpRequest request = new SendHttpRequest();
        byte[] response = request.sendRequestWithToken("POST", body, token, "http://localhost:9090/add/client");
        try {
            String res = new String(response);

            if (res.contentEquals("Paciente adicionado com sucesso")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Paciente adicionado");
                alert.setHeaderText("O paciente foi adicionado");
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
                            controller.passParameters(token,role, username);
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(postReg1);
                            stage.setScene(scene);
                            stage.show();
                            break;
                        case "wardClerk":
                            loader = new FXMLLoader(getClass().getResource("ward.fxml"));
                            Parent postReg2 = loader.load();
                            WardController wController = loader.getController();
                            wController.passParameters(token,role, username);
                            Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene2 = new Scene(postReg2);
                            stage2.setScene(scene2);
                            stage2.show();
                            break;
                    }

                }
            } else if (res.contentEquals("Os valores inseridos nao sao validos")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Paciente não adicionado");
                alert.setHeaderText("O paciente não  foi adicionado");
                alert.setResizable(false);
                alert.setContentText("Os valores que inseriu não são válidos");
                alert.show();

            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Paciente não adicionado");
            alert.setHeaderText("O paciente não  foi adicionado");
            alert.setResizable(false);
            alert.setContentText("Token expirou");
            alert.show();
        }
    }
    public void passParameters(String token,String role,String username) {
        this.token=token;
        this.role=role;
        this.username=username;

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
            case "wardClerk":
                loader = new FXMLLoader(getClass().getResource("ward.fxml"));
                Parent postReg2 = loader.load();
                WardController wController = loader.getController();
                wController.passParameters(token,role, username);
                Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene2 = new Scene(postReg2);
                stage2.setScene(scene2);
                stage2.show();
                break;
        }

    }
}




