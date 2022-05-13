package com.example.eventapp.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
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
import com.example.eventapp.data.Database.Event;
import com.example.eventapp.data.Database.Registration;
import com.example.eventapp.data.Database.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

public class ScannerActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST_CODE = 101;

    TextView txtView;
    CodeScanner qrScanner;
    CodeScannerView qrView;
    FirebaseFirestore fireDb;

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

        fireDb = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        String userName = i.getStringExtra("oName");

        setupPermissions();

        qrScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] parts = result.getText().split("&&");
                        String uName = parts[1];
                        String eName = parts[0];
                        txtView.setText("Event pass for " + eName + " for " + uName);

                        fireDb.collection("Registration").document(uName + " " + eName)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    Registration reg = documentSnapshot.toObject(Registration.class);
                                    if (reg.getEventId().equals(eName)) {
                                        if(reg.getAttendance().equals(0)) {
                                            fireDb.collection("Event").document(eName)
                                                    .get()
                                                    .addOnSuccessListener(documentSnapshot1 -> {
                                                        Event event = documentSnapshot1.toObject(Event.class);
                                                        String organizerId = event.getOrganizer_id();

                                                        if (organizerId.equals(userName)) {
                                                            fireDb.collection("Student").document(uName).get()
                                                                    .addOnSuccessListener(documentSnapshot2 -> {
                                                                        Student student = documentSnapshot2.toObject(Student.class);
                                                                        Integer points = student.getPoints();
                                                                        fireDb.collection("Student").document(uName).update("points", points + 1);
                                                                        fireDb.collection("Registration").document(uName + " " + eName)
                                                                                .update("attendance", 1);

                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        Toast.makeText(getApplicationContext(), "Error fetching event2", Toast.LENGTH_SHORT).show();
                                                                    });

                                                        }
                                                        else {
                                                            Toast.makeText(getApplicationContext(), "Different event", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getApplicationContext(), "Error fetching event3", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Attendance already done", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getApplicationContext(), "Error fetching event1", Toast.LENGTH_SHORT).show();
                                });



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