package com.example.eventapp.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetail extends AppCompatActivity {

    private static final String TAG = "EventDetail";

    Button registerEventButton, showPassButton;
    TextView eventNameText, instructionText, startDateText, endDateText, eventDescriptionText;
    FirebaseFirestore fireDb;

    String eventId, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        registerEventButton = (Button) findViewById(R.id.register_event_btn);
        showPassButton = (Button) findViewById(R.id.show_event_pass_btn);
        showPassButton.setVisibility(View.GONE);
        eventNameText = (TextView) findViewById(R.id.event_name_text);
        instructionText = (TextView) findViewById(R.id.instruction_text);
        startDateText = (TextView) findViewById(R.id.start_date_text);
        endDateText = (TextView) findViewById(R.id.end_date_text);
        eventDescriptionText = (TextView) findViewById(R.id.event_description_text);

        fireDb = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String[] details = intent.getExtras().getStringArray("details");

        CollectionReference eventsRef = fireDb.collection("Event");
        eventsRef.whereEqualTo("name", details[0])
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                eventId = document.getId();
                                userName = details[1];

                                Date startDate = document.getTimestamp("startDate").toDate();
                                Date endDate = document.getTimestamp("endDate").toDate();

                                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy @hh:mm");

                                startDateText.setText("Start: " + dateFormat.format(startDate));
                                endDateText.setText("End: " + dateFormat.format(endDate));

                                eventDescriptionText.setText((String) document.get("description"));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        eventNameText.setText(details[0]);
        instructionText.setText("Would you like to attend, " + details[1] + "?");

        registerEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerEventButton.setVisibility(View.GONE);
                showPassButton.setVisibility(View.VISIBLE);
            }
        });

        showPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                EventPassFragment f = new EventPassFragment(eventId, userName);
                ft.add(R.id.c_layout, f).addToBackStack(null);
                ft.commit();

                showPassButton.setVisibility(View.GONE);
            }
        });

    }
}