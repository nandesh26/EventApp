package com.example.eventapp.data;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.eventapp.R;

public class RegisterActivity extends AppCompatActivity {

    EditText nameData, userData, passData, emailData;
    RadioGroup typeGrp;
    RadioButton userType;
    Button regBtn;
    DBhelper db;

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

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int typeID = typeGrp.getCheckedRadioButtonId();
                userType = findViewById(typeID);
                String name = nameData.getText().toString();
                String username = userData.getText().toString();
                String email = emailData.getText().toString();
                String password = passData.getText().toString();
                boolean res = db.insertStudent(name,username,email,password,userType.getText().toString());
                if(!res) {
                    Toast.makeText(getApplicationContext(), "error inserting",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "successful inserting",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}