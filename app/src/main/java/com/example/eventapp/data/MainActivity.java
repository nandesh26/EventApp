package com.example.eventapp.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventapp.R;
import com.example.eventapp.data.Database.Organizer;
import com.example.eventapp.data.Database.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity {

    /*
    private static final String url = "jdbc:mysql://192.168.1.103:3306/eventapp";
    private static final String user = "nandesh";
    private static final String pass = "nandesh";
    */
    Button btnLogin, btnSignUp;
    EditText nameData, passData;
    DBhelper db;
    RadioGroup typegrp;
    RadioButton usertype;
    FirebaseFirestore fireDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = findViewById(R.id.signup);

        nameData = findViewById(R.id.username2);
        passData = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        db = new DBhelper(this);

        fireDb = FirebaseFirestore.getInstance();

        typegrp = findViewById(R.id.type2);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = typegrp.getCheckedRadioButtonId();
                usertype = findViewById(id);
                String type = usertype.getText().toString();
                String username = nameData.getText().toString();
                String password = passData.getText().toString();
//                db.checkUser(username,password,type);
                checkUser(username, password, type);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkUser(String username, String password, String type) {
        if (type.equals("Student")) {
            DocumentReference studentRef = fireDb.collection("Student").document(username);
            studentRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Student student = documentSnapshot.toObject(Student.class);
                        if (student != null) {
                            if (student.getPassword().equals(password)) {
                                Toast.makeText(getApplicationContext(), "Student exists", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), studentHome.class);
                                intent.putExtra("student", student);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Student does not exist", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else if (type.equals("Organizer")) {
            DocumentReference studentRef = fireDb.collection("Organizer").document(username);
            studentRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Organizer organizer = documentSnapshot.toObject(Organizer.class);
                        if (organizer != null) {
                            if (organizer.getPassword().equals(password)) {
                                Toast.makeText(getApplicationContext(), "Organizer exists", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), organizerHome.class);
                                intent.putExtra("organizer", organizer);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Organizer does not exist", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    /*
    private class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database connection success");

                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();

                if (params[0]=="fetch") {
                    result = result + "\n" + fetch_data(st);
                }
                else if (params[0]=="insert") {
                    String name = nameData.getText().toString();
                    String email = emailData.getText().toString();
                    String password = passData.getText().toString();
                    insert_data(st, name, email, password);
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        public String fetch_data(Statement st) {
            String result = "";
            try {
                ResultSet rs = st.executeQuery("select * from student");
                while (rs.next()) {
                    result += rs.getString("name") + " " + rs.getString("email") + "\n";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return result;
        }

        public void insert_data(Statement st, String name, String email, String password) {
            try {
                ResultSet rs = st.executeQuery("select count(id) from student");
                rs.next();
                int cnt = rs.getInt(1) + 1;
                String id = String.valueOf(cnt);
                String values = "(" + id +","+"'"+name+"'"+","+"'"+email+"'"+","+"'"+password+"'"+")";
                Log.i("APP","" + values);
                st.executeUpdate("insert into student values "+values);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            txtData.setText(result);
        }
    }

     */

}