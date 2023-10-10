package domain;

import cipher.CipherHandler;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * 
 * Handles Client-Server connectivity as well as transmitting and receiving of information
 */
public final class Client {

	private KeyStore keyStore;
	private static Client INSTANCE;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private Key serverPubKey;
	private SecretKey sessionKey;

	/**
	 * Returns the Client singleton instance
	 * @return Client singleton
	 */
	public static Client getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Client constructor
	 * @param address-server address
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 * @since 1.0
	 */
	private Client(String truststore, String keystore, String keystorePass) throws NumberFormatException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		Path cp = Paths.get("");
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream fis = new FileInputStream(keystore);
		keyStore.load(fis, keystorePass.toCharArray());
		
		privateKey = CipherHandler.getPrivateKeyFromKeyStorePath(keystore,keystorePass, "client", "JKS");
		publicKey = CipherHandler.getPublicKeyFromPathToKeyStore(keystore, keystorePass, "client", "JKS");
		serverPubKey = CipherHandler.getPublicKeyFromPathToKeyStore(truststore, "admin25", "server", "JKS");
	}
	
	/**
	 * Returns the Client singleton instance after creating the Client
	 * @return Client singleton
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 * @since 1.0
	 */
	public static Client loadClient(String truststore, String keystore, String keystorePass) throws NumberFormatException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		INSTANCE = new Client(truststore,keystore, keystorePass);
		return INSTANCE;
	}
	/**
	 * 
	 * @return keyStore
	 * 
	 */
	public KeyStore getKeyStore() {
		return keyStore;
	}
	/**
	 * 
	 * @return publicKey
	 * 
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}
	/**
	 * 
	 * @return privateKey
	 * 
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	/**
	 * 
	 * @return serverPubKey
	 * 
	 */
	public Key getServerPubKey() {
		return serverPubKey;
	}

	public void putSessionKey(SecretKey sessionKey) {
		this.sessionKey = sessionKey;
		
	}

	public SecretKey getSessionKey() {
		return sessionKey;
	}
	
}
