package com.example.sirs_gui;

import cipher.CipherHandler;
import domain.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setResizable(false);
        stage.setTitle("Hospital25");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            File file=new File("ClientKeystore.client");
            Client client = Client.loadClient("src/main/java/com/example/sirs_gui/ServerTrustStore","src/main/java/com/example/sirs_gui/ClientKeystore.client","admin25");
            SecretKey sessionKey= CipherHandler.generateKey("AES");
            client.putSessionKey(sessionKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
        launch();
    }
}
