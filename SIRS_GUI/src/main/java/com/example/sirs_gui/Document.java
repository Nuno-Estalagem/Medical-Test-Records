package com.example.sirs_gui;

import javafx.beans.property.SimpleStringProperty;

public class Document {
    private final SimpleStringProperty theme;
    private final SimpleStringProperty lab;
    private final SimpleStringProperty id;

    public Document(String theme, String lab,String id) {
        this.theme = new SimpleStringProperty(theme);
        this.lab = new SimpleStringProperty(lab);
        this.id=new SimpleStringProperty(id);
    }




    public String getTheme() {
        return theme.get();
    }


    public String getLab() {
        return lab.get();
    }

    public String getID(){
        return id.get();
    }

}

