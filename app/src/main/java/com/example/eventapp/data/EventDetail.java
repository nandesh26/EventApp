package com.example.eventapp.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.data.Database.Event;
import com.example.eventapp.data.Database.Registration;
import com.example.eventapp.data.Database.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventDetail extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "EventDetail";

    Button registerEventButton, showPassButton;
    TextView eventNameText, instructionText, startDateText, endDateText, eventDescriptionText, feeText;
    FirebaseFirestore fireDb;
    Event event;
    int student_price;

    String eventId, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Checkout.preload(getApplicationContext());

        registerEventButton = (Button) findViewById(R.id.register_event_btn);
        showPassButton = (Button) findViewById(R.id.show_event_pass_btn);
        showPassButton.setVisibility(View.GONE);
        eventNameText = (TextView) findViewById(R.id.event_name_text);
        instructionText = (TextView) findViewById(R.id.instruction_text);
        startDateText = (TextView) findViewById(R.id.start_date_text);
        endDateText = (TextView) findViewById(R.id.end_date_text);
        eventDescriptionText = (TextView) findViewById(R.id.event_description_text);
        feeText = (TextView) findViewById(R.id.event_fee_text);

//        if (savedInstanceState != null) {
//            registerEventButton.setEnabled(savedInstanceState.getBoolean("register-event-button-enabled"));
//            registerEventButton.setVisibility(savedInstanceState.getInt("register-event-button-visibility"));
//            showPassButton.setVisibility(savedInstanceState.getInt("show-pass-button-visibility"));
//        }


        fireDb = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String[] details = intent.getExtras().getStringArray("details");
        eventId = details[0];
        userName = details[1];

        CollectionReference eventsRef = fireDb.collection("Event");
        eventsRef.document(details[0])
                .get()
                .addOnSuccessListener(documentSnapshot -> {
//                    eventId = documentSnapshot.getId();
//                    userName = details[1];
                    Timestamp now = Timestamp.now();
                    event = documentSnapshot.toObject(Event.class);
                    if (event.getEndDate().compareTo(now) < 0 || (event.getStartDate().compareTo(now) < 0 && event.getEndDate().compareTo(now) > 0)) {
                        Toast.makeText(getApplicationContext(), "Event Registration ended", Toast.LENGTH_SHORT).show();
                        instructionText.setText("Event Registration has been closed");
                        registerEventButton.setEnabled(false);
                    }
                    else {
                        instructionText.setText("Would you like to attend, " + userName + "?");
                    }
                    eventNameText.setText(eventId);
                    Date startDate = event.getStartDate().toDate();
                    Date endDate = event.getEndDate().toDate();

                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy @hh:mm", Locale.ENGLISH);

                    startDateText.setText("Start: " + dateFormat.format(startDate));
                    endDateText.setText("End: " + dateFormat.format(endDate));
                    int regFee = event.getTicketPrice();
                    if(regFee > 0) {
                        fireDb.collection("Student").document(userName)
                                .get().addOnSuccessListener(documentSnapshot1 -> {
                                    Student student = documentSnapshot1.toObject(Student.class);
                                    int points = student.getPoints();
                                    int price = event.getTicketPrice();
                                    price = discountPrice(price, points);
                                    student_price = price;
                                    feeText.setText("Registration Fee: Rs." + String.valueOf(price) + "/-");
                        });
                    }
                    else {
                        feeText.setText("Free Registration !!");
                        }
                    eventDescriptionText.setText((String) event.getDescription());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error fetching event", Toast.LENGTH_SHORT).show();
                });
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                eventId = document.getId();
//                                userName = details[1];
//
//                                Date startDate = document.getTimestamp("startDate").toDate();
//                                Date endDate = document.getTimestamp("endDate").toDate();
//
//                                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy @hh:mm");
//
//                                startDateText.setText("Start: " + dateFormat.format(startDate));
//                                endDateText.setText("End: " + dateFormat.format(endDate));
//
//                                eventDescriptionText.setText((String) document.get("description"));
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

        fireDb.collection("Registration")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        List<Registration> reg = new ArrayList<>();
//                        System.out.println(userName + ", " + eventId);
//                        System.out.println("event found in registration");
//                        System.out.println(task.getResult().size());
                        for (QueryDocumentSnapshot doc: task.getResult()) {
                            System.out.println("for loop");
                            Registration registration = doc.toObject(Registration.class);
//                            reg.add(registration);
                            if (registration.getStudentUsername().equals(userName)) {
                                Toast.makeText(getApplicationContext(), userName + " already registered for this event", Toast.LENGTH_SHORT).show();
                                registerEventButton.setVisibility(View.GONE);
                                showPassButton.setVisibility(View.VISIBLE);
                            }
                        }
//                        System.out.println(reg);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error getting events", Toast.LENGTH_SHORT).show();
                    }
                });

        registerEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
                /*Registration newRegistration = new Registration(userName, eventId);
                fireDb.collection("Registration").document(userName + " " + eventId)
                        .set(newRegistration)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getApplicationContext(), "Event with " + eventId + " registered successfully", Toast.LENGTH_SHORT).show();
                            registerEventButton.setVisibility(View.GONE);
                            showPassButton.setVisibility(View.VISIBLE);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "Error in registration", Toast.LENGTH_SHORT).show();
                });*/
            }
        });

        showPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                EventPassFragment f = new EventPassFragment(eventId, userName);
                ft.add(R.id.c_layout, f).addToBackStack(null);
                ft.commit();

//                showPassButton.setVisibility(View.GONE);
            }
        });

    }

    private void startPayment(){

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_esQHo3tQFg08BV");
        /**
         * Set your logo here
         */
        //checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", event.getName());
            options.put("description", event.getDescription());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(student_price)+"00");//pass amount in currency subunits
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    private int discountPrice(int price, int points) {
        if (points >= 20) {
            price = (int)Math.round(0.8*price);
        }
        else if (points >= 15) {
            price = (int)Math.round(0.85*price);
        }
        else if (points >= 10) {
            price = (int)Math.round(0.9*price);
        }
        else if (points >= 5) {
            price = (int)Math.round(0.95*price);
        }
        return price;
    }

    @Override
    public void onPaymentSuccess(String s) {
        Registration newRegistration = new Registration(userName, eventId);
        fireDb.collection("Registration").document(userName + " " + eventId)
                .set(newRegistration)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Event with " + eventId + " registered successfully", Toast.LENGTH_SHORT).show();
                    registerEventButton.setVisibility(View.GONE);
                    showPassButton.setVisibility(View.VISIBLE);
                }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Error in registration", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onPaymentError(int i, String s) {

    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("show-pass-button-visibility", showPassButton.getVisibility());
//        outState.putInt("register-event-button-visibility", registerEventButton.getVisibility());
//        outState.putBoolean("register-event-button-enabled", registerEventButton.isEnabled());
//    }
}