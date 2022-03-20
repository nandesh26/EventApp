package com.example.eventapp.data;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventapp.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity {

    private static final String url = "jdbc:mysql://192.168.0.118:3306/eventapp";
    private static final String user = "nandesh";
    private static final String pass = "nandesh";
    Button btnFetch,btnClear,btnInsert;
    TextView txtData;
    EditText nameData, emailData, passData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtData = (TextView) this.findViewById(R.id.txtData);
        btnFetch = (Button) findViewById(R.id.btnFetch);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ConnectMySql connectMySql = new ConnectMySql();
                connectMySql.execute("fetch");
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtData.setText("");
            }
        });

        nameData = findViewById(R.id.name);
        emailData = findViewById(R.id.email);
        passData = findViewById(R.id.password);
        btnInsert = findViewById(R.id.insert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectMySql connectMySql = new ConnectMySql();
                connectMySql.execute("insert");
                Toast.makeText(MainActivity.this, "Insert clicked ...", Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }

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

}