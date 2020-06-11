package com.example.biometricauthentication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.net.ssl.HttpsURLConnection;

public class RestAPI {

    public String sendMacToAPI(String macAddress) throws Exception {
        HttpsURLConnection conn = HttpsConnectionFactory.getConnection(
                new URL("https://192.168.178.80:8443/mac"), "POST");
        HTTPSHandler handler = new HTTPSHandler(conn, macAddress);
        FutureTask task = new FutureTask<>(handler);
        Thread t = new Thread(task);
        t.start();

        return (String)task.get();
    }
}

class HTTPSHandler implements Callable<String>{

    private String message;
    private HttpsURLConnection connection;

    public HTTPSHandler(HttpsURLConnection connection, String message) {
        this.connection = connection;
        this.message = message;
    }

    private void writeOutputStream(HttpsURLConnection destination, String message) {
        try(OutputStream os = destination.getOutputStream()) {
            byte[] input = message.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        catch (Exception ex) {
            System.out.println("Error trying to write output stream: " + ex);
        }
    }

    private String readResponse(InputStream stream) {
        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("The response: " + response.toString());
        }
        catch (Exception ex) {
            System.out.println("Exception when reading response: " + ex);
        }
        return response.toString();
    }


    @Override
    public String call() throws Exception {
        writeOutputStream(connection, message);
        return readResponse(connection.getInputStream());
    }
}
