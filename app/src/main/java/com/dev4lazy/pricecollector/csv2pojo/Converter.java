package com.dev4lazy.pricecollector.csv2pojo;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.dev4lazy.pricecollector.utils.PermissionsUtils;

public class Converter {
    private static final String TAG = "Converter";

    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;

    private Csv2AnalysisRowConverter csv2AnalysisRowConverter;

    // todo usuń private Activity hostActivity = null;
    private Fragment hostFragment = null;

    /* todo usuń Konstruktor dla użycia Convertera w aktywności
    public Converter(Activity activity) {
        hostActivity = activity;
    }

     */

    // Konstruktor dla użycia Convertera we fragmencie
    public Converter(Fragment fragment) {
        hostFragment = fragment;
    }

    public void doConversion() {
        if (isPermissionGranted()) {
            prepare();
            convert();
        }
    }

    private boolean isPermissionGranted() {
        if (hostFragment!=null) {
            PermissionsUtils permissionsUtils = new PermissionsUtils(hostFragment);
            if (permissionsUtils.isPermissionNotGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionsUtils.callUserForPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        MY_PERMISSIONS_REQUEST_STORAGE
                );
                return false;
            }
            return true;
        }
        Log.e(TAG, " hostFragment równy null");
        return false;
    }

    private void prepare() {
        csv2AnalysisRowConverter = new Csv2AnalysisRowConverter("dane-test1000.csv");
    }

    private void convert() {
        csv2AnalysisRowConverter.makeAnalisisRowList();
        csv2AnalysisRowConverter.insertAllAnalysisRows();
    }
}
