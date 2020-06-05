package com.example.biometricauthentication;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class RestAPI {

    public String sendMacToAPI(String macAddress) throws Exception {

        Callable callable = new Task();
        FutureTask task = new FutureTask(callable);
        Thread t = new Thread(task);
        t.start();
        String password = (String) task.get();
        System.out.println("Executed task: " + password);
        return password;

    }
    class Task implements Callable<String> {
        // To Do : Add api url for getting a generated password.
        private String serverUrl = "https://ajax.googleapis.com/ajax/" +
                "services/search/web?v=1.0&q={query}";

        @Override
        public String call() throws Exception {
            RestTemplate rest = new RestTemplate();
            rest.getMessageConverters().add(new StringHttpMessageConverter());

            // Change this so it posts a MAC address to the api.
            String result = rest.getForObject(this.serverUrl, String.class, "Android");
            return result;
        }
    }

}
