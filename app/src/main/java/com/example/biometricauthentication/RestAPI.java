package com.example.biometricauthentication;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class RestAPI {

    public String sendMacToAPI(String macAddress) throws Exception {

        Task task = new Task();
        task.setMac(macAddress);
        FutureTask futureTasktask = new FutureTask(task);
        Thread t = new Thread(futureTasktask);
        t.start();
        String password = (String) futureTasktask.get();
        System.out.println("Executed task: " + password);
        return password;

    }
    class Task implements Callable<String> {
        // To Do : Add api url for getting a generated password.
        private String serverUrl = "http://192.168.178.80:8080/mac";
        private String mac = "";

        public void setMac(String mac) {
            this.mac = mac;
        }


        @Override
        public String call() throws Exception {
            RestTemplate rest = new RestTemplate();

            rest.getMessageConverters().add(new StringHttpMessageConverter());
            System.out.println("MAc address in task " + mac);
            // Change this so it posts a MAC address to the api.
            //String result = rest.getForObject(this.serverUrl, String.class, "Android");
            String result = rest.postForObject(this.serverUrl, this.mac, String.class);
            return result;
        }
    }

}
