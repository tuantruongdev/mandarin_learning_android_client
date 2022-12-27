package com.example.mandarinlearning.ui.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mandarinlearning.utils.ApplicationHelper;

public class BaseActivity extends AppCompatActivity {
    private PermissionHandler cb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationHelper.overrideAnimation(this, 0);
    }

    public void setActivityTitle(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ApplicationHelper.overrideAnimation(this, 1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                ApplicationHelper.overrideAnimation(this, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(BaseActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        cb.onGranted();
//                    }
//                } else {
//                    cb.onRejected();
//                }
//                return;
                boolean allGranted = true;
                if (permissions.length == 0) return;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                    }
                }
                if (allGranted) {
                    cb.onGranted();
                } else {
                    cb.onRejected();
                }
            }
        }
    }

    public void requestPermission(String[] permission, PermissionHandler cb) {
        this.cb = cb;
        boolean allGranted = true;
        //check if all permission granted
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(ApplicationHelper.getInstance().getContext(), permission[i]) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
            }
        }

        //if all granted then call handler(callback)
        if (allGranted) {
            //  ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
            cb.onGranted();
        }
        // else not all granted, then request not granted permission
        else {
            for (int i = 0; i < permission.length; i++) {
                // check if user declined
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission[i])) {
                    ActivityCompat.requestPermissions(this, permission, 1);
                } else {
                    ActivityCompat.requestPermissions(this, permission, 1);
                }
            }
        }


//        if (ContextCompat.checkSelfPermission(ApplicationHelper.getInstance().getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//                ActivityCompat.requestPermissions(this, permission, 1);
//            } else {
//                ActivityCompat.requestPermissions(this, permission, 1);
//            }
//        } else {
//            //granted
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
//            cb.onGranted();
//        }
    }
    public void openPermissionSetting(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}