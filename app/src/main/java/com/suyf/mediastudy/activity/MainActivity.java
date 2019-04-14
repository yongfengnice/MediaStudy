package com.suyf.mediastudy.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.suyf.mediastudy.R;
import com.suyf.mediastudy.audio.AudioActivity;
import com.suyf.mediastudy.camera.SurfaceCameraActivity;
import com.suyf.mediastudy.camera.TextureCameraActivity;

import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openAudioActivity(View view) {
        startActivity(new Intent(this, AudioActivity.class));
    }

    public void openSurfaceCameraActivity(View view) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new BasePermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        super.onPermissionGranted(response);
                        startActivity(new Intent(MainActivity.this, SurfaceCameraActivity.class));
                    }
                })
                .check();
    }

    public void openTextureCameraActivity(View view) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new BasePermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        super.onPermissionGranted(response);
                        startActivity(new Intent(MainActivity.this, TextureCameraActivity.class));
                    }
                })
                .check();
    }
}
