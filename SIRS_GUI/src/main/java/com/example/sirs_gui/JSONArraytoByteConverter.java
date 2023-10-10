package com.example.sirs_gui;

import org.json.JSONArray;

public class JSONArraytoByteConverter {

    public JSONArraytoByteConverter(){
    }

    public byte[] convertJSONArrayToByte(JSONArray array){
        byte[] data = new byte[array.length()];
        for (int i = 0; i < array.length(); i++) {
            data[i]=(byte)(((int)array.get(i)) & 0xFF);
        }
        return data;
    }
}
