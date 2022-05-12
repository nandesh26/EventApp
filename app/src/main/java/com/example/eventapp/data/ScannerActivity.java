package com.example.eventapp.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.eventapp.R;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST_CODE = 101;

    TextView txtView;
    CodeScanner qrScanner;
    CodeScannerView qrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        txtView = findViewById(R.id.passDetail);
        txtView.setText("Scan Pass");
        qrView = findViewById(R.id.scanner_view);
        qrScanner = new CodeScanner(this,qrView);
        qrScanner.setScanMode(ScanMode.CONTINUOUS);

//        txtView = findViewById(R.id.passDetail);
//        qrView = findViewById(R.id.scanner_view);
//        qrScanner = new CodeScanner(this,qrView);

        setupPermissions();

        qrScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtView.setText(result.getText());
                        // Toast.makeText(ScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        qrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrScanner.startPreview();
    }

    @Override
    protected void onPause() {
        qrScanner.releaseResources();
        super.onPause();
    }

    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }
}