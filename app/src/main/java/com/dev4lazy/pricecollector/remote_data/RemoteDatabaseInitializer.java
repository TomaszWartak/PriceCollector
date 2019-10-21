package com.dev4lazy.pricecollector.remote_data;

import android.Manifest;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.dev4lazy.pricecollector.model.RemoteDataRepository;
import com.dev4lazy.pricecollector.utils.PermissionsUtils;

public class RemoteDatabaseInitializer {
    private static final String TAG = "RemoteDatabaseInitializer";

    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;

    private Csv2EanCodeConverter csv2EanCodeConverter;
    private Csv2AnalysisRowConverter csv2AnalysisRowConverter;

    // todo usuń private Activity hostActivity = null;
    private Fragment hostFragment = null;

    /* todo usuń Konstruktor dla użycia Convertera w aktywności
    public RemoteDatabaseInitializer(Activity activity) {
        hostActivity = activity;
    }

     */

    // Konstruktor dla użycia Convertera we fragmencie
    public RemoteDatabaseInitializer(Fragment fragment) {
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
        RemoteDataRepository.getInstance().clearDatabase();
        csv2AnalysisRowConverter = new Csv2AnalysisRowConverter();
        csv2EanCodeConverter = new Csv2EanCodeConverter();
    }

    private void convert() {
        csv2AnalysisRowConverter.makeAnalisisRowList();
        csv2AnalysisRowConverter.insertAllAnalysisRows();
        csv2EanCodeConverter.makeEanCodeList();
        csv2EanCodeConverter.insertAllEanCodes();
    }
}
