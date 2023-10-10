package com.example.sirs_gui;

import cipher.CipherHandler;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import domain.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import com.auth0.jwt.JWT;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class LoginController {
    @FXML
    private Button button;
    @FXML
    private Label wrong;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ImageView userPhoto;
    @FXML
    private ImageView lockerPhoto;
    @FXML
    private ImageView thirdPhoto;
    @FXML
    private AnchorPane anchorPane;


    private String token;
    private String role;

    public LoginController(){
    }

    public void userLogin(ActionEvent event) throws Exception{
            int wasAuthenticated = checkLogin();
        if (wasAuthenticated==-1){
            wrong.setText("Insert your username and password!");
        }
        else if (wasAuthenticated==-2){
            Alert wrongCredentials=new Alert(Alert.AlertType.ERROR);
            wrongCredentials.setContentText("Invalid Credentials!");
            wrongCredentials.show();
        }
        else{

            DecodedJWT decode = JWT.decode(token.replaceAll("^\"+|\"+$", ""));
            Claim claim = decode.getClaim("resource_access");
            this.role = claim.toString().split(":")[2].split("\"")[1];
            setAfterLogin(token,role,event);

        }


    }


    private int checkLogin()  {
        //LoginApplication application= new LoginApplication();
        String usName = username.getText();
        String pwd = password.getText();
        if (usName.isBlank() || pwd.isBlank()) {
            return -1;
        } else {
                try {
                    String jsonToken = sendRequest(usName, pwd);
                    if (jsonToken.equals(""))
                        return -2;
                    //Retrieve Token Role
                    else
                        this.token=jsonToken.replaceAll("\"","");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            return 0;
        }

        }
        //return -2;
    private String sendRequest(String username , String password) throws Exception {
        try {
            Client client = Client.getInstance();
            byte[] key = CipherHandler.encryptKey(client.getSessionKey(), client.getServerPubKey());

            JSONObject params=new JSONObject();
            params.put("username", username);
            params.put("pass", password);

            JSONObject message=new JSONObject();
            byte[] payload = params.toString().getBytes();
            payload = CipherHandler.encrypt(payload, client.getSessionKey());
            message.put("sessionKey", key);
            message.put("params", payload);
            JSONObject body = new JSONObject();
            body.put("message", message);
            body.put("messageHash", CipherHandler.hashMessage(message.toString().getBytes()));
            System.out.println(Arrays.toString(CipherHandler.hashMessage(message.toString().getBytes())));
            SendHttpRequest request= new SendHttpRequest();
            byte[] response= request.sendRequest(body,"http://localhost:9090/login");
            String resposta= new String(response);
                return resposta;
            }catch(Exception e) {
            e.printStackTrace();

            return "";        }


    }

    private void setAfterLogin(String token, String role, ActionEvent event) throws IOException {
        if (role.contentEquals("admin")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_menu_view.fxml"));
            Parent postLogin = loader.load();
            AdminController controller = loader.getController();
            controller.passParameters(token, role,username.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(postLogin);
            stage.setScene(scene);
            stage.show();

        } else if (role.contentEquals("wardClerk")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ward.fxml"));
            Parent postLogin = loader.load();
            WardController controller = loader.getController();
            controller.passParameters(token, role,username.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(postLogin);
            stage.setScene(scene);
            stage.show();

        } else if (role.contentEquals("doctor") ) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-menu-view.fxml"));
            Parent postLogin = loader.load();
            DoctorController controller = loader.getController();
            controller.passParameters(token, role,username.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(postLogin);
            stage.setScene(scene);
            stage.show();

        } else if (role.contentEquals("partnerLab")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("partnerLab.fxml"));
            Parent postLogin = loader.load();
            PartnerLabController controller = loader.getController();
            controller.passParameters(token, role,username.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(postLogin);
            stage.setScene(scene);
            stage.show();

        }else if(role.contentEquals("nurse") || role.contentEquals("porter") || role.contentEquals("ca") || role.contentEquals("psa")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("porter_ca_nurse.fxml"));
            Parent postLogin = loader.load();
            PorterController controller = loader.getController();
            controller.passParameters(token, role,username.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(postLogin);
            stage.setScene(scene);
            stage.show();

        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("volunteer.fxml"));
            Parent postLogin = loader.load();
            VolunteerController controller = loader.getController();
            controller.passParameters(token, role);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(postLogin);
            stage.setScene(scene);
            stage.show();

        }


        }



}

