package domain;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import cipher.CipherHandler;

import javax.crypto.SecretKey;

/**
 *
 */
public class Server {

    private static Server INSTANCE = null;
    public static final String KEY_STORE_ALIAS = "server";
    public static final String KEY_STORE_TYPE = "JKS";
    private static final String KEYSTORE_PATH = "/KeyStore.jks";
    private HashMap<String, SecretKey> sessionKeys=new HashMap<>();
    private final String keyStorePassword;
    private final KeyStore keyStore;



    public static Server getInstance() throws Exception{
        if (INSTANCE==null){
            Server.create(new File("").getAbsolutePath().concat(KEYSTORE_PATH),"admin25");
        }
        return INSTANCE;
    }

    /**
     *
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @since 2.0
     */
    public static Server create(String keyStorePath, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        INSTANCE = new Server(keyStorePath,keyStorePassword);
        return INSTANCE;

    }

    /**
     *
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @since 1.0
     */
    private Server( String keyStorePath, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        this.keyStorePassword = keyStorePassword;
        keyStore = CipherHandler.getKeyStore(keyStorePath, keyStorePassword, KEY_STORE_TYPE);
    }


    public String getPassword() {
        return keyStorePassword;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public Key getSessionKey(String token) throws KeyStoreException {

            return sessionKeys.get(token);

    }
    public void setSessionKey(String token, SecretKey sessionKey){
        sessionKeys.put(token,sessionKey);
    }
}
