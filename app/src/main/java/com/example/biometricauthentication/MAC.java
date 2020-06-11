package com.example.biometricauthentication;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.appcompat.app.AppCompatActivity;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MAC extends AppCompatActivity {

    public static String getMAC(Context context)
    {
        DeviceAdminReceiver admin = new DeviceAdminReceiver();
        DevicePolicyManager devicepolicymanager = admin.getManager(context);
        List<ComponentName> lijstje = devicepolicymanager.getActiveAdmins();
        System.out.println("DIT IS DE LIJST" + " " + lijstje);
        ComponentName name1 = lijstje.get(2);

        String mac = "";
        if (devicepolicymanager.isAdminActive(name1)) {
            mac = devicepolicymanager.getWifiMacAddress(name1);
            System.out.println("macAddress" + mac);
        }
        return mac;
    }
}
