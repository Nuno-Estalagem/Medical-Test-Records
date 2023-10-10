package com.example.sirs_gui;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import cipher.CipherHandler;
import domain.Client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SendHttpRequest {

    public SendHttpRequest() {

    }

    public byte[] sendRequest(JSONObject parameters, String url) throws Exception {
        Client client = Client.getInstance();
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.addHeader("content-type", "application/json");
        StringEntity params = new StringEntity(parameters.toString());
        post.setEntity(params);
        HttpResponse response = httpclient.execute(post);
        
        //Response
        HttpEntity entity = response.getEntity();
        InputStream instream = entity.getContent();
        JSONObject jsonResponse = new JSONObject(new String(instream.readAllBytes()));
        System.out.println(jsonResponse);
        JSONArraytoByteConverter jsonConverter = new JSONArraytoByteConverter();
        JSONObject message=jsonResponse.getJSONObject("message");

        byte []hash =message.toString().getBytes();
        byte[] messageHash= jsonConverter.convertJSONArrayToByte(jsonResponse.getJSONArray("messageHash"));
        if (!CipherHandler.verifyHash(hash,messageHash)) {
			return null;
		}
        JSONArray tok= message.getJSONArray("payload");
        byte[] data= jsonConverter.convertJSONArrayToByte(tok);
        byte[] decipheredMsg = CipherHandler.decrypt(data, client.getSessionKey());
        return decipheredMsg;
    }

    public byte[] sendRequestWithToken(String type,JSONObject parameters, String token, String url) throws Exception {
        HttpClient httpclient = HttpClients.createDefault();
        Client client = Client.getInstance();
        if (type.contentEquals("POST")) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Bearer " + token);
            post.addHeader("content-type", "application/json");
            long time=new Timestamp(System.currentTimeMillis()).getTime();
            JSONObject header= new JSONObject();
            header.put("TimeStamp",CipherHandler.encrypt(String.valueOf(time).getBytes(),client.getSessionKey()));
            post.addHeader("Timestamp", header.toString());
            byte[] cipheredMsg = CipherHandler.encrypt(parameters.toString().getBytes(), client.getSessionKey());
            JSONObject body = new JSONObject();
            body.put("message", cipheredMsg);
            body.put("messageHash", CipherHandler.hashMessage(cipheredMsg));
            StringEntity params = new StringEntity(body.toString());
            post.setEntity(params);
            System.out.println(body);
            HttpResponse response = httpclient.execute(post);

            try {
                //Response
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();

                JSONObject jsonResponse = new JSONObject(new String(instream.readAllBytes()));
                System.out.println(jsonResponse);
                JSONArraytoByteConverter jsonConverter = new JSONArraytoByteConverter();
                JSONObject message = jsonResponse.getJSONObject("message");

                byte[] hash = message.toString().getBytes();
                byte[] messageHash = jsonConverter.convertJSONArrayToByte(jsonResponse.getJSONArray("messageHash"));
                if (!CipherHandler.verifyHash(hash, messageHash)) {
                    return null;
                }
                JSONArray payload = message.getJSONArray("payload");
                byte[] data = jsonConverter.convertJSONArrayToByte(payload);
                byte[] decipheredMsg = CipherHandler.decrypt(data, client.getSessionKey());
                return decipheredMsg;
            }catch (Exception e){
                return null;
            }
        }
        else if (type.contentEquals("GET")) {
            HttpGet get=new HttpGet(url);
            get.setHeader("Authorization", "Bearer " + token);
            long time=new Timestamp(System.currentTimeMillis()).getTime();
            JSONObject header= new JSONObject();
            header.put("TimeStamp",CipherHandler.encrypt(String.valueOf(time).getBytes(),client.getSessionKey()));
            get.addHeader("Timestamp", header.toString());
            HttpResponse response = httpclient.execute(get);

            try {
                //Response
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();

                JSONObject jsonResponse = new JSONObject(new String(instream.readAllBytes()));
                System.out.println(jsonResponse+" herrreeee");
                JSONArraytoByteConverter jsonConverter = new JSONArraytoByteConverter();
                JSONObject message = jsonResponse.getJSONObject("message");

                byte[] hash = message.toString().getBytes();
                byte[] messageHash = jsonConverter.convertJSONArrayToByte(jsonResponse.getJSONArray("messageHash"));
                if (!CipherHandler.verifyHash(hash, messageHash)) {
                    return null;
                }
                JSONArray payload = message.getJSONArray("payload");
                byte[] data = jsonConverter.convertJSONArrayToByte(payload);
                byte[] decipheredMsg = CipherHandler.decrypt(data, client.getSessionKey());
                return decipheredMsg;
            }catch (Exception e){
                return null;
            }
        }
        return null;
    }
}