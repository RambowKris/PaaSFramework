package com.example.krb.myawsapplication.Supporting;

import android.os.Build;

/**
 * Created by krb on 27/08/2017.
 */

public class UtilityManager {

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }
}
