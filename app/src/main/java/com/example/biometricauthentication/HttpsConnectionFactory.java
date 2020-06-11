package com.example.biometricauthentication;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class HttpsConnectionFactory {

    private URL url;
    private static SSLSocketFactory sslSocketFactory;

    public static void setSslSocketFactory(SSLSocketFactory factory) {
        sslSocketFactory = factory;
    }

    public static HttpsURLConnection getConnection(URL url, String method) {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslSocketFactory);
            conn.setRequestMethod(method);
            if(method.equals("POST")) {
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);
            }
            return conn;
        }
        catch (Exception ex) {
            System.out.println("Error building connection: " + ex);
        }
        // Todo: fix return type
        return null;
    }

}
