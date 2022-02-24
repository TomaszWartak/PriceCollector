package com.dev4lazy.pricecollector.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.Resources;
import android.os.Build;

import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.dev4lazy.pricecollector.R;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.pm.PermissionInfo.PROTECTION_DANGEROUS;
import static android.content.pm.PermissionInfo.PROTECTION_FLAG_PRIVILEGED;
import static android.content.pm.PermissionInfo.PROTECTION_NORMAL;
import static android.content.pm.PermissionInfo.PROTECTION_SIGNATURE;
import static android.content.pm.PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM;


public class PermissionsUtils {

    public static final int ALL_PERMISSIONS_REQUEST = 1;

    private String[] applicationPermissions = {
            // Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA /*,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS*/
    };

    private Fragment hostFragment;
    public PermissionsUtils( Fragment fragment ) {
        hostFragment = fragment;
    }

    public String getProtectionLevelForPermission( String permission ) {
        Context context = hostFragment.getContext();
        PackageManager packageManager = context.getPackageManager();
        String protectionLevelDescription = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("android", PackageManager.GET_PERMISSIONS );
            if (packageInfo.permissions != null) {
                // For each defined permission
                for (PermissionInfo permissionInfo : packageInfo.permissions) {
                    String permissionName = permissionInfo.name;
                    String permissionGroupName = permissionInfo.group;
                    if ( permission.equals( permissionName )) {
                        int dummyInt = permissionInfo.describeContents();
                        /**
                         * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
                         * For example, if the object will include a file descriptor in the output of writeToParcel(android.os.Parcel, int),
                         * the return value of this method must include the CONTENTS_FILE_DESCRIPTOR bit.
                         */

                        int protectionLevel;
                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.P /* 28 */ ) {
                            protectionLevel = permissionInfo.getProtection();
                            /**
                             * PermissionInfo.getProtection() returns a bitmask indicating the set of special object types
                             * marshaled by this Parcelable object instance. Value is either 0 or CONTENTS_FILE_DESCRIPTOR
                             */
                        } else {
                            protectionLevel = permissionInfo.protectionLevel;
                        }

                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.P /* 28 */ ) {
                            dummyInt = permissionInfo.getProtectionFlags();
                            /**
                             * PermissionInfo.getProtectionFlags() returns value is either 0 or a combination of PROTECTION_FLAG_PRIVILEGED, PROTECTION_FLAG_SYSTEM,
                             * PROTECTION_FLAG_DEVELOPMENT, PROTECTION_FLAG_APPOP, PROTECTION_FLAG_PRE23, PROTECTION_FLAG_INSTALLER,
                             * PROTECTION_FLAG_VERIFIER, PROTECTION_FLAG_PREINSTALLED, PROTECTION_FLAG_SETUP, PROTECTION_FLAG_INSTANT,
                             * PROTECTION_FLAG_RUNTIME_ONLY, android.content.pm.PermissionInfo.PROTECTION_FLAG_OEM,
                             * android.content.pm.PermissionInfo.PROTECTION_FLAG_VENDOR_PRIVILEGED,
                             * android.content.pm.PermissionInfo.PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER,
                             * android.content.pm.PermissionInfo.PROTECTION_FLAG_WELLBEING,
                             * android.content.pm.PermissionInfo.PROTECTION_FLAG_DOCUMENTER,
                             * android.content.pm.PermissionInfo.PROTECTION_FLAG_CONFIGURATOR,
                             * android.content.pm.PermissionInfo.PROTECTION_FLAG_INCIDENT_REPORT_APPROVER,
                             * and android.content.pm.PermissionInfo.PROTECTION_FLAG_APP_PREDICTOR
                             */
                        } else {
                            // todo ok: ???
                        }

                        String dummyString = permissionInfo.loadDescription( packageManager ).toString();
                        /**
                         * Parameters: pm	PackageManager: A PackageManager from which the label can be loaded;
                         * usually the PackageManager from which you originally retrieved this item. This value must never be null.
                         * Returns:
                         * CharSequence	Returns a CharSequence containing the permission's description. If there is no description, null is returned.
                         */

                        switch (protectionLevel) {
                            case PROTECTION_NORMAL:
                                protectionLevelDescription = "normal";
                                break;
                            case PROTECTION_DANGEROUS:
                                protectionLevelDescription = "dangerous";
                                break;
                            case PROTECTION_SIGNATURE:
                                protectionLevelDescription = "signature";
                                break;
                            case PROTECTION_SIGNATURE_OR_SYSTEM: // deprecated in API 28 (P)
                                protectionLevelDescription = "signature or system";
                                break;
                            case PROTECTION_SIGNATURE|PROTECTION_FLAG_PRIVILEGED: // from API 28 (P)
                                protectionLevelDescription = "signature or protection flag privileged";
                                break;
                            default:
                                protectionLevelDescription = "<unknown>";
                                break;
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        }
        return protectionLevelDescription;
    }

    public void askUserForPermission(String permission, int permisionRequest ){
        if ( hostFragment!=null) {
            hostFragment.requestPermissions( new String[]{permission}, permisionRequest ) ;
        }
    }

    public void askUserForPermissions(String[] permissions, int permissionRequest ) {
        if ( hostFragment!=null ) {
            hostFragment.requestPermissions( permissions, permissionRequest );
        }
    }

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

    public boolean isPermissionNotGranted( String permission ) {
        return !isPermissionGranted( permission );
    }


    public boolean isPermissionGranted( String permission ) {
            if (hostFragment != null) {
                return PermissionChecker.checkSelfPermission(hostFragment.getActivity(), permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        return false;
    }

    public String getConcatenatedPermisionsNames(String[] permissions ) {
        String permissionsNames = "";
        for ( String permissionName : permissions) {
            permissionsNames = permissionsNames + permissionName + ", ";
        }
        return permissionsNames;
    }

    public String getConcatenatedPermisionsNames(ArrayList<String> permissions ) {
        String permissionsNames = "";
        for ( String permissionName : permissions) {
            permissionsNames = permissionsNames + permissionName + ", ";
        }
        return permissionsNames;
    }

    public String getConcatenatedShortPermisionsNames( ArrayList<String> permissions ) {
        String shortPermissionsNames = "";
        for ( String permissionName : permissions) {
            shortPermissionsNames =
                    shortPermissionsNames +
                    "- " + convertPermissionName2LocaleLanguage( permissionName ) +
                    ";" +
                    "\n";
        }
        return shortPermissionsNames;
    }

    public String convertPermissionName2LocaleLanguage( String permissionName ) {
        Resources resources = hostFragment.getContext().getResources();
        String localePermissionName = resources.getString( R.string.not_translated );
        switch ( permissionName ) {
            case Manifest.permission.CAMERA:
                localePermissionName = resources.getString( R.string.short_permission_name_camera );
                break;
            case Manifest.permission.INTERNET:
                localePermissionName = resources.getString( R.string.short_permission_name_internet );
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                localePermissionName = resources.getString( R.string.short_permission_name_read_external_storage );
                break;
            case Manifest.permission.READ_CONTACTS:
                localePermissionName = resources.getString(R.string.short_permission_name_contacts);
                break;
            case Manifest.permission.GET_ACCOUNTS:
                localePermissionName = resources.getString( R.string.short_permission_name_user_account );
                break;
        }
        return localePermissionName;
    }

    public ArrayList<String> getDeniedPermissions() {
        ArrayList<String> neededPermissions = new ArrayList<>();
        for ( String permission : applicationPermissions) {
            if (isPermissionNotGranted( permission )) {
                neededPermissions.add( permission );
            }
        }
        return neededPermissions;
    }

    public ArrayList<String> getDeniedPermissionsFromPermissionsArray(String[] permissions, int[] grantResults) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for ( int permissionNr=0; permissionNr<grantResults.length; permissionNr++ ) {
            if ( grantResults[ permissionNr ] == PackageManager.PERMISSION_DENIED ) {
                deniedPermissions.add( permissions[ permissionNr ] );
            }
        }
        return deniedPermissions;
    }

    public ArrayList<String> getManifestPermissions() {
        ArrayList<String> manifestPermissions = new ArrayList<>();
        Context context = hostFragment.getContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            PackageInfo info = packageManager.getPackageInfo( packageName, PackageManager.GET_PERMISSIONS );
            if (info.requestedPermissions != null) {
                manifestPermissions =  new ArrayList<>( Arrays.asList(info.requestedPermissions) );
            }
        } catch (Exception e) { // NameNotFoundException
            e.printStackTrace();
        }
        return manifestPermissions;
    }

}
