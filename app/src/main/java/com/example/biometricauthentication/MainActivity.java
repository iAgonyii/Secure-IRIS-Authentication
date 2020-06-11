package com.example.biometricauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.Executor;
import com.example.biometricauthentication.MAC;

public class MainActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private CustomTrustManager manager;
    private Biometrics biometrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        manager = new CustomTrustManager();

        InputStream input = getResources().openRawResource(getResources().getIdentifier("bma_new", "raw", getPackageName()));
        manager.loadCA(input, "password");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_login);
        executor = ContextCompat.getMainExecutor(this);
        biometrics = new Biometrics(executor, this);
        biometricPrompt = biometrics.getInstance();

        Button biometricLoginButton = findViewById(R.id.test);
            biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(biometrics.buildPrompt());
        });
    }
}