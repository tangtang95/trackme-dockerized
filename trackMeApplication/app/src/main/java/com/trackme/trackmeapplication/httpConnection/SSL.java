package com.trackme.trackmeapplication.httpConnection;

import com.trackme.trackmeapplication.baseUtility.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Static context where it is possible load the ssl configuration and the keystore.
 *
 * @author Mattia Tibaldi
 */
public class SSL {

    private static SSL instance = null;

    private SSLContext sslContext;
    private HostnameVerifier hostnameVerifier;

    /**
     * Private constructor.
     */
    private SSL(){}

    public static SSL getInstance(){
        if(instance == null)
            instance = new SSL();
        return instance;
    }

    /**
     * Set up the ssl context and load the keystore
     *
     * @param keyStoreIn keystore in the raw folder.
     */
    public void setUpSSLConnection(InputStream keyStoreIn) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(Constant.KEY_STORE_TYPE);
            keyStore.load(keyStoreIn, Constant.password);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            hostnameVerifier = (hostname, session) -> hostname.equals(Settings.getServerAddress());

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException |
                KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method
     *
     * @return the ssl context
     */
    SSLContext getSslContext() {
        return sslContext;
    }

    /**
     * Getter method
     *
     * @return the host name verifier
     */
    HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }
}
