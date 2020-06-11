package com.example.biometricauthentication;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

public class Biometrics {

    private Executor executor;
    private FragmentActivity activity;
    private RestAPI api;


    public Biometrics(Executor executor, FragmentActivity activity){
        this.executor = executor;
        this.activity = activity;
        api = new RestAPI();
    }

    private BiometricPrompt.AuthenticationCallback createCallback() {

        return new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(activity.getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(activity.getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                String mac = MAC.getMAC(activity.getApplicationContext());
                // To do: send to rest api
                api = new RestAPI();
                try {
                    System.out.println("MAc address" + mac);
                    String password = api.sendMacToAPI(mac);

                    System.out.println("Generated password " + password);
                    Toast.makeText(activity.getApplicationContext(), password, Toast.LENGTH_LONG).show();
                    //TextView pwbox = (TextView)findViewById(R.id.passwordText);
                    //pwbox.setText(password);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(activity.getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        };

    }

    public BiometricPrompt.PromptInfo buildPrompt() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in using your biometric credentials")
                .setNegativeButtonText("Use account password")
                .build();
    }

    public BiometricPrompt getInstance() {
        return new BiometricPrompt(activity, executor, createCallback());
    }


}
