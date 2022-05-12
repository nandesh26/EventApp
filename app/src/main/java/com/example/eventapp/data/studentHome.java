package com.example.eventapp.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.data.Database.Event;
import com.example.eventapp.data.Database.Student;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class studentHome extends AppCompatActivity {

    Button pastBtn, ongoingBtn, upcomingBtn;
    SwitchMaterial registeredEventsSwitch;

    Student student;
    String timeline = "recommended";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        pastBtn = findViewById(R.id.student_past_events_btn);
        ongoingBtn = findViewById(R.id.student_ongoing_events_btn);
        upcomingBtn = findViewById(R.id.student_upcoming_events_btn);
        registeredEventsSwitch = findViewById(R.id.student_registered_events_switch);

        Intent i = getIntent();
        student = i.getParcelableExtra("student");

        updateFragment(timeline, registeredEventsSwitch);

        System.out.println(registeredEventsSwitch.isChecked());

        registeredEventsSwitch.setOnClickListener(view -> {
            updateFragment(timeline, registeredEventsSwitch);
        });

        pastBtn.setOnClickListener(view -> {
            System.out.println(registeredEventsSwitch.isChecked());
            updateFragment("past", registeredEventsSwitch);
            timeline = "past";
//            Bundle bd = new Bundle();
//            bd.putString("pushed-by", "student");
//            bd.putBoolean("registered-event-switch-status", registeredEventsSwitch.isChecked());
//            System.out.println(registeredEventsSwitch.isChecked());
//            bd.putString("event-timeline", "past");
//            f = new Show_Events_List_Fragment(student.getUsername());
//            f.setArguments(bd);
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.student_events_list_fragment_view, f);
//            ft.commit();
//            Toast.makeText(getApplicationContext(), "New fragment for showing past events for student", Toast.LENGTH_SHORT).show();
        });

        ongoingBtn.setOnClickListener(view -> {
            System.out.println(registeredEventsSwitch.isChecked());
            updateFragment("ongoing", registeredEventsSwitch);
            timeline = "ongoing";
//            Bundle bd = new Bundle();
//            bd.putString("pushed-by", "student");
//            bd.putBoolean("registered-events-switch-status", registeredEventsSwitch.isChecked());
//            bd.putString("event-timeline", "ongoing");
//            f = new Show_Events_List_Fragment(student.getUsername());
//            f.setArguments(bd);
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.student_events_list_fragment_view, f);
//            ft.commit();
//            Toast.makeText(getApplicationContext(), "New fragment for showing ongoing events for student", Toast.LENGTH_SHORT).show();
        });

        upcomingBtn.setOnClickListener(view -> {
            System.out.println(registeredEventsSwitch.isChecked());
            updateFragment("upcoming", registeredEventsSwitch);
            timeline = "upcoming";
//            Bundle bd = new Bundle();
//            bd.putString("pushed-by", "student");
//            bd.putBoolean("registered-events-switch-status", registeredEventsSwitch.isChecked());
//            bd.putString("event-timeline", "upcoming");
//            f = new Show_Events_List_Fragment(student.getUsername());
//            f.setArguments(bd);
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.student_events_list_fragment_view, f);
//            ft.commit();
//            Toast.makeText(getApplicationContext(), "New fragment for showing upcoming events for student", Toast.LENGTH_SHORT).show();
        });

    }

    private void updateFragment(String timeline, SwitchMaterial registeredEventsSwitch) {
        Bundle bd = new Bundle();
        bd.putString("pushed-by", "student");
        bd.putBoolean("registered-events-switch-status", registeredEventsSwitch.isChecked());
        bd.putString("event-timeline", timeline);
        Show_Events_List_Fragment f = new Show_Events_List_Fragment(student.getUsername(), getApplicationContext());
        f.setArguments(bd);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.student_events_list_fragment_view, f);
        ft.commit();
        Toast.makeText(getApplicationContext(), "New fragment for showing " + timeline + " events for student", Toast.LENGTH_SHORT).show();
    }
}