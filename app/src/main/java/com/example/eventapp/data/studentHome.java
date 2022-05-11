package com.example.eventapp.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class studentHome extends AppCompatActivity {

    Button pastBtn, ongoingBtn, upcomingBtn;
    SwitchMaterial registeredEventsSwitch;
    Show_Events_List_Fragment f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        pastBtn = findViewById(R.id.student_past_events_btn);
        ongoingBtn = findViewById(R.id.student_ongoing_events_btn);
        upcomingBtn = findViewById(R.id.student_upcoming_events_btn);
        registeredEventsSwitch = findViewById(R.id.student_registered_events_switch);

        Intent i = getIntent();
        int student_id = Integer.parseInt(i.getStringExtra("student_id"));
        System.out.println(registeredEventsSwitch.isChecked());

        pastBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println(registeredEventsSwitch.isChecked());
                Bundle bd = new Bundle();
                bd.putString("pushed-by", "student");
                bd.putBoolean("registered-event-switch-status", registeredEventsSwitch.isChecked());
                System.out.println(registeredEventsSwitch.isChecked());
                bd.putString("event-timeline", "past");
                f = new Show_Events_List_Fragment(student_id);
                f.setArguments(bd);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.student_events_list_fragment_view, f);
                ft.commit();
                Toast.makeText(getApplicationContext(), "New fragment for showing past events for student", Toast.LENGTH_SHORT).show();
            }
        });

        ongoingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(registeredEventsSwitch.isChecked());
                Bundle bd = new Bundle();
                bd.putString("pushed-by", "student");
                bd.putBoolean("registered-events-switch-status", registeredEventsSwitch.isChecked());
                bd.putString("event-timeline", "ongoing");
                f = new Show_Events_List_Fragment(student_id);
                f.setArguments(bd);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.student_events_list_fragment_view, f);
                ft.commit();
                Toast.makeText(getApplicationContext(), "New fragment for showing ongoing events for student", Toast.LENGTH_SHORT).show();
            }
        });

        upcomingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(registeredEventsSwitch.isChecked());
                Bundle bd = new Bundle();
                bd.putString("pushed-by", "student");
                bd.putBoolean("registered-events-switch-status", registeredEventsSwitch.isChecked());
                bd.putString("event-timeline", "upcoming");
                f = new Show_Events_List_Fragment(student_id);
                f.setArguments(bd);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.student_events_list_fragment_view, f);
                ft.commit();
                Toast.makeText(getApplicationContext(), "New fragment for showing upcoming events for student", Toast.LENGTH_SHORT).show();
            }
        });

    }
}