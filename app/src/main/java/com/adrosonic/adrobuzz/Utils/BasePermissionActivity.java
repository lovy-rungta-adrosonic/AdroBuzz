package com.adrosonic.adrobuzz.Utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Lovy on 11-05-2018.
 */

public abstract class BasePermissionActivity extends AppCompatActivity implements OnPermissionCallback {

    private static final String TAG = BasePermissionActivity.class.getSimpleName();

    private PermissionHelper permissionHelper;

    private AlertDialog builder;
    private String[] neededPermission;

//    private final static String SINGLE_PERMISSION = Manifest.permission.RECORD_AUDIO;

    private final static String[] MULTI_PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionHelper = PermissionHelper.getInstance(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public abstract void updateWithPermission(boolean granted);

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        updateWithPermission(true);
        Log.v(TAG, "Permission(s) " + Arrays.toString(permissionName) + " Granted");
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        Toast.makeText(this, "Permission Denied, You cannot record audio and access storage", Toast.LENGTH_SHORT).show();
        updateWithPermission(false);
        Log.v(TAG, "Permission(s) " + Arrays.toString(permissionName) + " Declined");
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        updateWithPermission(true);
        Log.v(TAG, "Permission( " + permissionsName + " ) preGranted");
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        neededPermission = PermissionHelper.declinedPermissions(this, MULTI_PERMISSIONS);
        StringBuilder builder = new StringBuilder(neededPermission.length);
        if (neededPermission.length > 0) {
            for (String permission : neededPermission) {
                builder.append(permission).append("\n");
            }
        }
        Log.v(TAG,"Permission( " + builder.toString() + " ) needs Explanation");
        AlertDialog alert = getAlertDialog(neededPermission, builder.toString());
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        Toast.makeText(this,  "Required permission(s) can only be granted from settings screen", Toast.LENGTH_SHORT).show();
        Log.v(TAG,  " Required permission(s) can only be granted from settings screen");
        updateWithPermission(false);
    }

    @Override
    public void onNoPermissionNeeded() {
        updateWithPermission(true);
        Log.v(TAG, "Permission(s) not needed");
    }

    public void checkForPermission() {
        if (permissionHelper != null) {
//            if (!permissionHelper.isPermissionGranted(SINGLE_PERMISSION)) {
//
//                if (permissionHelper.isExplanationNeeded(SINGLE_PERMISSION)) {
//
//                    permissionHelper.requestAfterExplanation(SINGLE_PERMISSION);
//
//                } else {
                    permissionHelper
                            .setForceAccepting(false) // default is false. its here so you know that it exists.
                            .request(MULTI_PERMISSIONS);
//                }
//
            }
            else{
                updateWithPermission(true);
            }
        }

    public AlertDialog getAlertDialog(final String[] permissions, final String permissionName) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this)
                    .setTitle("Permission Needs Explanation")
                    .create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionHelper.requestAfterExplanation(permissions);
            }
        });
        builder.setMessage("Permissions need explanation (" + permissionName + ")");
        return builder;
    }
}


