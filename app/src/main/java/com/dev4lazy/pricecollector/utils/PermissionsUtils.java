package com.dev4lazy.pricecollector.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class PermissionsUtils {
    private Fragment hostFragment;

    /* todo usuń Konstruktor dla użycia onRequestPermissionsResult w aktywności
    // public PermissionsUtils(Activity activity) {
        hostActivity = activity;
    }
     */

    // Konstruktor dla użycia onRequestPermissionsResult we fragmencie
    public PermissionsUtils(Fragment fragment) {
        hostFragment = fragment;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean isPermissionNotGranted(String permission) {
        return !isPermissionGranted(permission);
    }

    public boolean isPermissionGranted(String permission) {
            if (hostFragment != null) {
                return ContextCompat.checkSelfPermission(hostFragment.getActivity(), permission)
                        == PackageManager.PERMISSION_GRANTED;
            }
        return false;
    }

    public void callUserForPermission(String permission, int permisionRequest){
            if (hostFragment!=null) {
                hostFragment.requestPermissions(
                        new String[]{permission},
                        permisionRequest);
            }
    }

}
