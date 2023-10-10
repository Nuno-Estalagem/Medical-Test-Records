package Quarkus.Group25;

import cipher.CipherHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Key;
import java.sql.Timestamp;

public class OperationsWithSessionKey {

    public OperationsWithSessionKey(){

    }

    public JSONObject decipher(Key sessionKey, String request,String timestamp) throws Exception{
        JSONArraytoByteConverter jsonConverter = new JSONArraytoByteConverter();
        JSONObject body = new JSONObject(request);

        byte [] message =jsonConverter.convertJSONArrayToByte(body.getJSONArray("message"));
        byte[] messageHash= jsonConverter.convertJSONArrayToByte(body.getJSONArray("messageHash"));
        if (!CipherHandler.verifyHash(message, messageHash) || !compareStamps(timestamp,sessionKey)) {
            return null;
        }




        byte[] deciphered= CipherHandler.decrypt(message,sessionKey);

        return new JSONObject (new String(deciphered));
    }

    public byte[] cipher(byte[] payload, Key sessionKey) throws Exception{
        JSONObject message=new JSONObject();
        payload = CipherHandler.encrypt(payload,sessionKey);
        message.put("payload",payload);
        JSONObject returnedBody= new JSONObject();
        returnedBody.put("message", message);
        returnedBody.put("messageHash", CipherHandler.hashMessage(message.toString().getBytes()));

        return returnedBody.toString().getBytes();
    }

    public boolean compareStamps(String timeStamp, Key sessionKey) throws Exception{
        JSONObject object= new JSONObject(timeStamp);
        JSONArray array=object.getJSONArray("TimeStamp");
        JSONArraytoByteConverter converter= new JSONArraytoByteConverter();
        byte[] cipher=converter.convertJSONArrayToByte(array);
        byte[] deciphered= CipherHandler.decrypt(cipher,sessionKey);

        long currentTime= new Timestamp(System.currentTimeMillis()).getTime();
        System.out.println(new String(deciphered));
        long requestTime = Long.parseLong(new String(deciphered));
        System.out.println(Math.abs(currentTime-requestTime));
        return Math.abs(currentTime-requestTime) < 3000;
    }
}
