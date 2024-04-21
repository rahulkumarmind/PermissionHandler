package com.rahul.permissionhelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionHelper {
    private static PermissionHelper instance;
    private final Context context;
    private String[] permissions;
    private PermissionResultCallback callback;
    private ActivityResultLauncher<String[]> permissionLauncher;

    public interface PermissionResultCallback {
        void onPermissionResult(boolean allPermissionsGranted, List<String> deniedPermissions);

        void onShouldShowRationale(List<String> deniedPermissions);
    }

    private PermissionHelper(Context context) {
        this.context = context;
    }

    public static synchronized PermissionHelper getInstance(Context context) {
        if (instance==null) {
            instance = new PermissionHelper(context);
        }
        instance.init((AppCompatActivity) context);
        return instance;
    }

    public void init(AppCompatActivity context) {
        permissionLauncher =
                context.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            List<String> deniedPermissions = new ArrayList<>();
            boolean shouldShowRationale = false;

            for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                if (!entry.getValue()) {
                    deniedPermissions.add(entry.getKey());
                    if (context.shouldShowRequestPermissionRationale(entry.getKey())) {
                        shouldShowRationale = true;
                    }
                }
            }

            if (deniedPermissions.isEmpty()) {
                callback.onPermissionResult(true, deniedPermissions);
            } else if (shouldShowRationale) {
                callback.onShouldShowRationale(deniedPermissions);
            } else {
                callback.onPermissionResult(false, deniedPermissions);
            }
        });
    }

    public void checkAndRequestPermissions(String[] permissions,
                                           PermissionResultCallback callback) {
        this.permissions = permissions;
        this.callback = callback;
        List<String> neededPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)!=android.content.pm.PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }

        if (!neededPermissions.isEmpty()) {
            permissionLauncher.launch(neededPermissions.toArray(new String[0]));
        } else {
            callback.onPermissionResult(true, new ArrayList<>());
        }
    }

    public void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}