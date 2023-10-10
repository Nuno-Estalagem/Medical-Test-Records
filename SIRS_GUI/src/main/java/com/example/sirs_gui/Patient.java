package com.example.sirs_gui;

import javafx.beans.property.SimpleStringProperty;

public class Patient {
    private final SimpleStringProperty nif;
    private final SimpleStringProperty name;
    private final SimpleStringProperty age;

    public Patient(String fName, String lName, String email) {
        this.nif = new SimpleStringProperty(fName);
        this.name = new SimpleStringProperty(lName);
        this.age = new SimpleStringProperty(email);
    }

    public String getNif() {
        return nif.get();
    }


    public String getName() {
        return name.get();
    }


    public String getAge() {
        return age.get();
    }

}