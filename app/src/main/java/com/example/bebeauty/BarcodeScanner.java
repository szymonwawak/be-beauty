package com.example.bebeauty;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeScanner extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        initSources();
        initScanner();
    }

    private void initSources() {
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void checkPermissions() throws IOException {
        if (hasPermissions()) {
            cameraSource.start(surfaceView.getHolder());
            Toast toast = Toast.makeText(getApplicationContext(), "Nakieruj kamerę na kod kreskowy", Toast.LENGTH_LONG);
            toast.show();
        } else {
            ActivityCompat.requestPermissions(BarcodeScanner.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private boolean hasPermissions() {
        return ActivityCompat.checkSelfPermission(BarcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void initScanner() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        CameraSource.Builder cameraSourceBuilder = new CameraSource.Builder(this, barcodeDetector);
        cameraSourceBuilder.setRequestedPreviewSize(1920, 1080);
        cameraSourceBuilder.setAutoFocusEnabled(true);
        cameraSource = cameraSourceBuilder.build();
        SurfaceHolder.Callback surfaceHolderCallback = getSurfaceHolderCallback();
        surfaceView.getHolder().addCallback(surfaceHolderCallback);
        barcodeDetector.setProcessor(getDetector());
    }

    private SurfaceHolder.Callback getSurfaceHolderCallback() {
        return new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                runIfPermissionGranted();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        };
    }

    private void runIfPermissionGranted() {
        try {
            checkPermissions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private Detector.Processor<Barcode> getDetector() {
        return new Detector.Processor<Barcode>() {

            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    finish();
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            runIfPermissionGranted();
        else
            finish();
    }
}
