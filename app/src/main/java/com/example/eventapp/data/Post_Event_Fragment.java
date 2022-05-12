package com.example.eventapp.data;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.data.Database.Event;
import com.example.eventapp.data.Database.Organizer;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Post_Event_Fragment extends Fragment {

    DatePickerDialog datePickerDialog;
    EditText ename;
    EditText edesc;
    Button epostbutton, startBtn, endBtn, startTimebtn, endTimebtn;
    DBhelper dbhelp;
    String startDate, endDate, startTime, endTime;
    int s_id=0, e_id=0, hour, minute;

    String user_id;
    FirebaseFirestore fireDb;

    Post_Event_Fragment(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireDb = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_post__event_, container, false);
        dbhelp = new DBhelper(v.getContext());
        ename = (EditText) v.findViewById(R.id.ename);
        edesc = (EditText) v.findViewById(R.id.edesc);
        epostbutton = (Button) v.findViewById(R.id.epostbutton);
        startBtn = v.findViewById(R.id.startDateBtn);
        endBtn = v.findViewById(R.id.endDateBtn);
        startTimebtn = v.findViewById(R.id.startTimeBtn);
        endTimebtn = v.findViewById(R.id.endTimeBtn);
        initDatePicker(v);
        startBtn.setText(getTodayDate());
        endBtn.setText(getTodayDate());

        startBtn.setOnClickListener(view -> {
            s_id = 1;
            datePickerDialog.show();
        });

        endBtn.setOnClickListener(view -> {
            e_id = 1;
            datePickerDialog.show();
        });

        startTimebtn.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                {   String h, m;
                    hour = selectedHour;
                    minute = selectedMinute;
                    if (hour<10) {
                        h = "0" + hour;
                    }
                    else {
                        h = String.valueOf(hour);
                    }
                    if (minute<10) {
                        m = "0" + minute;
                    }
                    else{
                        m = String.valueOf(minute);
                    }
                    startTime = h + ":" + m;
                    startTimebtn.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                }
            };

            // int style = AlertDialog.THEME_HOLO_DARK;

            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

            timePickerDialog.setTitle("Select Start Time");
            timePickerDialog.show();

        });

        endTimebtn.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                {
                    String h, m;
                    hour = selectedHour;
                    minute = selectedMinute;
                    if (hour<10) {
                        h = "0" + String.valueOf(hour);
                    }
                    else {
                        h = String.valueOf(hour);
                    }
                    if (minute<10) {
                        m = "0" + String.valueOf(minute);
                    }
                    else{
                        m = String.valueOf(minute);
                    }
                    endTime = h + ":" + m;
                    endTimebtn.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                }
            };

            // int style = AlertDialog.THEME_HOLO_DARK;

            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

            timePickerDialog.setTitle("Select End Time");
            timePickerDialog.show();
        });

        epostbutton.setOnClickListener(view -> {
            startDate = startBtn.getText().toString();
            endDate = endBtn.getText().toString();
            try {
                Date startDateFormatted = (new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)).parse(startDate + " " + startTime);
                Date endDateFormatted = (new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)).parse(endDate + " " + endTime);
                if (startDateFormatted.compareTo(endDateFormatted) >= 0) {
                    Toast.makeText(requireContext(), "End Date should be after the start date", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println(startDateFormatted + ", " + endDateFormatted);
                    Event event = new Event(ename.getText().toString(),
                            edesc.getText().toString(),
                            new Timestamp(startDateFormatted),
                            new Timestamp(endDateFormatted),
                            user_id);

                    fireDb.collection("Event")
                            .document(event.getName())
                            .set(event)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(requireContext(), "Event added successfully", Toast.LENGTH_SHORT).show();
                                Button organizer_show_button = requireActivity().findViewById(R.id.organizer_show_button);
                                organizer_show_button.performClick();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Error adding event", Toast.LENGTH_SHORT).show();
                            });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            boolean res = dbhelp.insertEvent(ename.getText().toString(), edesc.getText().toString(), startDate + " " + startTime, endDate + " " + endTime, id);
//            if(!res) {
//                Toast.makeText(v.getContext(), "Error in inserting",Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(v.getContext(), "Successfully inserted event",Toast.LENGTH_SHORT).show();
//
//            }
        });

        return v;

    }

    private String makeDateString(int day, int month, int year)
    {
        String d = String.valueOf(day);
        String m = String.valueOf(month);
        String y = String.valueOf(year);
        if (day<10) {
            d = "0" + d;
        }
        if (month<10) {
            m = "0" + m;
        }

        return d + "-" + m + "-" + y;
    }

    private void initDatePicker(View v) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                if (s_id == 1) {
                    s_id = 0;
                    startBtn.setText(date);
                }
                else if(e_id == 1 ) {
                    e_id = 0;
                    endBtn.setText(date);
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(v.getContext(), style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String getTodayDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
}


