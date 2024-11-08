package com.example.zenithevents.HelperClasses;

import android.provider.Settings;
import android.content.Context;

public class DeviceUtils {

    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
