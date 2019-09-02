package com.dev4lazy.pricecollector.csv2pojo;

import android.Manifest;
import android.app.Activity;
import com.dev4lazy.pricecollector.utils.PermissionsUtils;

public class Converter {
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;

    private Csv2AnalysisRowConverter csv2AnalysisRowConverter;

    private Activity hostActivity = null;

    public Converter(Activity activity) {
        hostActivity = activity;
        if (isPermissionGranted()) {
            prepare();
            convert();
        }
    }

    public boolean isPermissionGranted() {
        PermissionsUtils permissionsUtils = new PermissionsUtils(hostActivity);
        if (permissionsUtils.isPermissionNotGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionsUtils.callUserForPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    MY_PERMISSIONS_REQUEST_STORAGE
            );
            return false;
        }
        return true;
    }

    public void prepare() {
        csv2AnalysisRowConverter = new Csv2AnalysisRowConverter("dane-test1000.csv", hostActivity);
    }

    public void convert() {
        csv2AnalysisRowConverter.makeAnalisisRowList();
        csv2AnalysisRowConverter.insertAllAnalysisRows();
    }
}
