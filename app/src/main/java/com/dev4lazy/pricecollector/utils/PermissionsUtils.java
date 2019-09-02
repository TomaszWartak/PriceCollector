package com.dev4lazy.pricecollector.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class PermissionsUtils {
    Activity hostActivity;
    public PermissionsUtils(Activity activity) {
        hostActivity = activity;
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(hostActivity, permission )
                == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isPermissionNotGranted(String permission) {
        return !isPermissionGranted(permission);
    }

    public void callUserForPermission(String permission, int permisionRequest){
        ActivityCompat.requestPermissions(
                hostActivity,
                new String[]{permission},
                permisionRequest);
    }

}
