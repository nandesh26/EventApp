package com.example.eventapp.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventapp.R;

import org.w3c.dom.Text;

public class EventDetail extends AppCompatActivity {

    Button regBtn, passBtn;
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        regBtn = (Button) findViewById(R.id.registerEventBtn);
        passBtn = (Button) findViewById(R.id.eventPassBtn);
        passBtn.setVisibility(View.GONE);
        txtView = (TextView) findViewById(R.id.details);

        Intent intent = getIntent();
        String[] details = intent.getExtras().getStringArray("details");

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtView.setText( details[0] + " " + details[1] + " " + details[2]);
                regBtn.setVisibility(View.GONE);
                passBtn.setVisibility(View.VISIBLE);
            }
        });

        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                EventPassFragment  f = new EventPassFragment();

                ft.add(R.id.c_layout, f).addToBackStack(null);
                ft.commit();
                passBtn.setVisibility(View.GONE);
            }
        });

    }
}