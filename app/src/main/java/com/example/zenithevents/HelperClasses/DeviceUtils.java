package com.example.zenithevents.HelperClasses;

import android.provider.Settings;
import android.content.Context;

/**
 * Utility class for retrieving device-specific information.
 * <p>
 * Note: The Javadocs for this class were generated with the assistance of an AI language model.
 * </p>
 *
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>Retrieve a unique device ID specific to the Android device.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The methods in this class can be helpful in applications where device identification
 * is needed for user authentication, analytics, or other device-specific functionalities.
 * </p>
 */
public class DeviceUtils {

    /**
     * Retrieves a unique device ID for the Android device.
     * <p>
     * The device ID is fetched from the Android secure settings, providing a unique identifier
     * for the device. This ID remains constant unless the device is factory reset.
     * </p>
     *
     * @param context The context of the calling component, used to access the secure settings.
     * @return A unique string ID representing the Android device.
     */
    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
