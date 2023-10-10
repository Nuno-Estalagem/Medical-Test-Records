package Quarkus.Group25;

import cipher.CipherHandler;
import domain.Server;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.security.PermitAll;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.ws.rs.*;
import java.io.*;
import java.security.PrivateKey;

@Produces("application/json")
@Path("/login")
public class LoginRequest {

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String hospitalRealm;
    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String secret;
    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientID;

    @POST
    @PermitAll
    @Consumes("application/json")
    public byte[] allParams(String request) throws Exception {
        JSONArraytoByteConverter jsonConverter = new JSONArraytoByteConverter();
        Server server = Server.getInstance();
        JSONObject body = new JSONObject(request);

        byte []hash =body.getJSONObject("message").toString().getBytes();
        byte[] messageHash= jsonConverter.convertJSONArrayToByte(body.getJSONArray("messageHash"));

        if (!CipherHandler.verifyHash(hash, messageHash)) {
        	return null;
        }


        

        JSONObject messageBody = body.getJSONObject("message");
        JSONArray keyJson = (JSONArray) messageBody.get("sessionKey");
        byte[] keyBytes = jsonConverter.convertJSONArrayToByte(keyJson);

        JSONArray array = (JSONArray) messageBody.get("params");
        byte[] paramsBytes = jsonConverter.convertJSONArrayToByte(array);

        PrivateKey pk = CipherHandler.getPrivateKeyFromKeyStore(server.getKeyStore(), "admin25", "server");

        SecretKey sessionKey = (SecretKey) CipherHandler.decryptKey(keyBytes, Cipher.SECRET_KEY, pk, "AES");

        byte[] parameters = CipherHandler.decrypt(paramsBytes, sessionKey);

        JSONObject realParameters = new JSONObject(new String(parameters));
        String username = realParameters.getString("username");
        String password = realParameters.getString("pass");
        String[] args = new String[]{"curl", "-X", "POST", hospitalRealm + "/protocol/openid-connect/token",
                "--user", "hospital-documents:" + secret,
                "-H", "Content-Type: application/x-www-form-urlencoded",
                "-d", "username=" + username,
                "-d", "password=" + password,
                "-d", "grant_type=password",
                "-d", "client_id=" + clientID};
        Process process = new ProcessBuilder(args).start();

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }


        JSONObject jsonToken = new JSONObject(output.toString());
        try {
            String token = jsonToken.getString("access_token");
            server.setSessionKey(token, sessionKey);


            JSONObject message=new JSONObject();
            byte[] payload = token.getBytes();
            OperationsWithSessionKey session= new OperationsWithSessionKey();
            return session.cipher(payload,server.getSessionKey(token));



        }catch (org.json.JSONException e){
            return CipherHandler.encrypt(output.toString().getBytes(), sessionKey);
        }

    }

}
