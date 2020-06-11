package com.example.biometricauthentication;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class CustomTrustManager {

    private KeyStore keyStore;

    public void loadCA(InputStream file, String password) {
        try {
            KeyStore ks = KeyStore.getInstance("pkcs12");
            ks.load(file, password.toCharArray());
            Enumeration e = ks.aliases();

            while (e.hasMoreElements()) {
                String alias = (String) e.nextElement();
                X509Certificate c = (X509Certificate) ks.getCertificate(alias);
                addCertificateToKeyStore(c);
            }
            createTrustManager();
        }
        catch (Exception e) {
            System.out.println("Exception occurred when loading certificate: " + e.getMessage());
        }
    }

    private void addCertificateToKeyStore(X509Certificate c) {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            keyStore.setCertificateEntry("BMA", c);
        }
        catch (Exception e) {
            System.out.println("Exception occurred when adding certificate: " + e.getMessage());
        }
    }

    private void createTrustManager() {
        try{
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            HttpsConnectionFactory.setSslSocketFactory(context.getSocketFactory());
        }
        catch (Exception e){
            System.out.println("Exception occurred when creating trust manager: " + e);
        }
    }
}
