package com.rahul.main;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rahul.permissionhelper.PermissionHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionHelper.PermissionResultCallback {

    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        permissionHelper = PermissionHelper.getInstance(this);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.checkAndRequestPermissions(new String[]{}, MainActivity.this);
            }
        });
    }

    @Override
    public void onPermissionResult(boolean allPermissionsGranted, List<String> deniedPermissions) {

    }

    @Override
    public void onShouldShowRationale(List<String> deniedPermissions) {

    }
}