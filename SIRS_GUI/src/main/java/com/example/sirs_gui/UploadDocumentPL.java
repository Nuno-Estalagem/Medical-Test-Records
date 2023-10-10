package com.example.sirs_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UploadDocumentPL {


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
            /*
            JSONObject json = new JSONObject();
            JSONArray patientsArray = new JSONArray();
            JSONObject patientInfo = new JSONObject();
            patientInfo.put("id", nifText);
            patientInfo.put("name", nameText);
            patientsArray.put(patientInfo);
            JSONArray recordsArray = new JSONArray();
            JSONObject recordInfo = new JSONObject();
            recordInfo.put("title",titleText);
            recordInfo.put("observations",observationsText);
            recordInfo.put("theme",themeText);
            recordsArray.put(recordInfo);
            json.put("patients", patientsArray);
            json.put("records",recordsArray);

            String message = json.toString();
            System.out.println(message);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("message", message));
            SendHttpRequest request = new SendHttpRequest();
            InputStream instream = request.sendRequestWithToken("POST", params, token, "http://localhost:9090/upload/Document");
        */
                JSONObject params = new JSONObject();
                params.put("nif", nifText);
                params.put("name", nameText);
                params.put("theme", themeText);
                params.put("title", titleText);
                params.put("observations", observationsText);
                params.put("username", username);

                SendHttpRequest request = new SendHttpRequest();
                byte[] response = request.sendRequestWithToken("POST", params, token, "http://localhost:9090/upload/plDocument");

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
                        FXMLLoader loader =  new FXMLLoader(getClass().getResource("partnerLab.fxml"));
                                Parent postReg = loader.load();
                                PartnerLabController pController = loader.getController();
                                pController.passParameters(token,role,username);
                                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                Scene scene = new Scene(postReg);
                                stage.setScene(scene);
                                stage.show();
                        }


                    }

                else if(response.toString().contentEquals("[Não existe nenhum paciente associado a esse NIF]")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Documento não criado");
                    alert.setHeaderText("O Documento não foi criado");
                    alert.setResizable(false);
                    alert.setContentText("Não existe nenhum paciente associado a esse NIF");
                    alert.show();

                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Documento não criado");
                    alert.setHeaderText("O Documento não foi criado");
                    alert.setResizable(false);
                    alert.setContentText("O paciente com esse nome não possui esse NIF");
                    alert.show();
                }
            }




        }

        public void goBack(ActionEvent actionEvent) throws Exception {
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("partnerLab.fxml"));
            Parent postReg = loader.load();
            PartnerLabController pController = loader.getController();
            pController.passParameters(token, role,username);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(postReg);
            stage.setScene(scene);
            stage.show();
        }


}

