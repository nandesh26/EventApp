package com.example.eventapp.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.eventapp.R;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {

    TextView txtView;
    CodeScanner qrScanner;
    CodeScannerView qrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        txtView = findViewById(R.id.passDetail);
        qrView = findViewById(R.id.scanner_view);
        qrScanner = new CodeScanner(this,qrView);

        qrScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
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
}