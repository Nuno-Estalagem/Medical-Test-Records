package com.example.sirs_gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class VolunteerController {
    @FXML
    private Label role;

    private String token;

    public void passParameters(String token, String passedRole) {
        this.token=token;
        role.setText(passedRole);
    }
}
