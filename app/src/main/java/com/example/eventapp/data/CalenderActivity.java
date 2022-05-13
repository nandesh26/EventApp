package com.example.eventapp.data;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eventapp.R;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity {
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dfm = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    FirebaseFirestore fireDb;
    RecyclerView calenderEventsRecView;
    StudentEventsListAdapter adapter;
    String user_id;
    List<com.example.eventapp.data.Database.Event> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        Intent i = getIntent();
        user_id = i.getStringExtra("user-id");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setTitle(null);

        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        adapter = new StudentEventsListAdapter(this, user_id);

        fireDb = FirebaseFirestore.getInstance();
        fireDb.collection("Event")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<com.example.eventapp.data.Database.Event> list = new ArrayList<>();
                        for (DocumentSnapshot doc: task.getResult()) {
                            com.example.eventapp.data.Database.Event event = doc.toObject(com.example.eventapp.data.Database.Event.class);
                            list.add(event);
                            Event ev = new Event(Color.YELLOW, event.getStartDate().toDate().getTime(), event.getName());
                            compactCalendar.addEvent(ev);
                        }
                        eventsList = list;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error getting events", Toast.LENGTH_SHORT).show();
                    }
                });

//        Long long_time=1653059558000L;
//
//        Event ev1 = new Event(Color.YELLOW, long_time, "Teachers' Professional Day");
//        Event ev2 = new Event(Color.YELLOW, long_time, "Teachers' Professional Day 2");
//        compactCalendar.addEvent(ev1);
//        compactCalendar.addEvent(ev2);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateClicked);
                Context context = getApplicationContext();
                System.out.println(dateClicked.toString());
//                List<Event> events_for_date=compactCalendar.getEvents(dateClicked);
                List<com.example.eventapp.data.Database.Event> dayEventsList = new ArrayList<>();
                for (com.example.eventapp.data.Database.Event e: eventsList) {
                    Calendar eCal = Calendar.getInstance();
                    eCal.setTime(e.getStartDate().toDate());
                    if (cal.get(Calendar.DAY_OF_MONTH) == eCal.get(Calendar.DAY_OF_MONTH)
                            && cal.get(Calendar.MONTH) == eCal.get(Calendar.MONTH)
                            && cal.get(Calendar.YEAR) == eCal.get(Calendar.YEAR)) {
                        dayEventsList.add(e);
                    }
                }
                calenderEventsRecView = findViewById(R.id.calenderEventsRecView);
                adapter.setEventsList(dayEventsList);
                calenderEventsRecView.setAdapter(adapter);
                calenderEventsRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        false));

//                TextView tv_eventdata=findViewById(R.id.eventdata);
//                tv_eventdata.setText(event_data);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                ab.setTitle(dfm.format(firstDayOfNewMonth));
            }
        });
    }
}


