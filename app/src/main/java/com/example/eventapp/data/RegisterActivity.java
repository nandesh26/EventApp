package com.example.eventapp.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.data.Database.Organizer;
import com.example.eventapp.data.Database.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText nameData, userData, passData, emailData;
    RadioGroup typeGrp;
    RadioButton userType;
    Button regBtn;
    DBhelper db;

    FirebaseFirestore fireDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameData = findViewById(R.id.name2);
        userData = findViewById(R.id.username);
        emailData = findViewById(R.id.email2);
        passData = findViewById(R.id.password2);
        typeGrp = findViewById(R.id.type);
        regBtn = findViewById(R.id.register);
        db = new DBhelper(this);

        
        fireDb = FirebaseFirestore.getInstance();

        regBtn.setOnClickListener(view -> {
            int typeID = typeGrp.getCheckedRadioButtonId();
            userType = ((RadioButton) findViewById(typeID));
            String name = nameData.getText().toString();
            String username = userData.getText().toString();
            String email = emailData.getText().toString();
            String password = passData.getText().toString();
//                boolean res = db.insertStudent(name,username,email,password,userType.getText().toString());
//                Map<String, Object> student = createUser(name, username, email, password);

            if(name.equals("") || username.equals("") || email.equals("") || password.equals("") || userType == null)
            {
                Toast.makeText(this, "Please enter missing details !!", Toast.LENGTH_SHORT).show();
            }
            else if(name.equals(username))
            {
                Toast.makeText(this, "Name and Username should not be same !!", Toast.LENGTH_SHORT).show();
            }
            else if(!email.contains("@iiitd.ac.in"))
            {
                Toast.makeText(this, "Please enter the official email of IIITD !!", Toast.LENGTH_SHORT).show();
            }
            else {
                String uType = userType.getText().toString();
                if (uType.equals("Student")) {
                    Student student = new Student(name, username, password, email, 0);
                    DocumentReference studentRef = fireDb.collection("Student")
                            .document(username);
                    studentRef.get().addOnCompleteListener(task -> {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            studentRef.set(student)
                                    .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(),
                                            "Student " + username + " added successfully",
                                            Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                                            "Error adding student",
                                            Toast.LENGTH_SHORT).show());
                        }
                    });
                    //                    if (!studentRef)
                    //                            .set(student)
                    //                            .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(),
                    //                                    "Student " + username + " added successfully",
                    //                                    Toast.LENGTH_SHORT).show())
                    //                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                    //                                    "Error adding student",
                    //                                    Toast.LENGTH_SHORT).show());
                } else if (uType.equals("Organizer")) {
                    Organizer organizer = new Organizer(name, username, password, email);
                    DocumentReference organizerRef = fireDb.collection("Organizer")
                            .document(username);
                    organizerRef.get().addOnCompleteListener(task -> {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            organizerRef.set(organizer)
                                    .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(),
                                            "Organizer " + username + " added successfully",
                                            Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                                            "Error adding organizer",
                                            Toast.LENGTH_SHORT).show());
                        }
                    });
                }
                //                if(!res) {
                //                    Toast.makeText(getApplicationContext(), "error inserting",Toast.LENGTH_SHORT).show();
                //                }
                //                else {
                //                    Toast.makeText(getApplicationContext(), "successful inserting",Toast.LENGTH_SHORT).show();
                //                }
            }
        });
    }
}