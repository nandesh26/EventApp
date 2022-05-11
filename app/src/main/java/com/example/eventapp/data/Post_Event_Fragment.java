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

import java.util.Calendar;
import java.util.Locale;

public class Post_Event_Fragment extends Fragment {

    DatePickerDialog datePickerDialog;
    EditText ename;
    EditText edesc;
    Button epostbutton, startBtn, endBtn, startTimebtn, endTimebtn;
    DBhelper dbhelp;
    String startDate, endDate, startTime, endTime;
    int id, s_id=0, e_id=0, hour, minute;

    Post_Event_Fragment(int id)
    {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        startBtn.setText(getTodaysDate());
        endBtn.setText(getTodaysDate());

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_id = 1;
                datePickerDialog.show();
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_id = 1;
                datePickerDialog.show();
            }
        });

        startTimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                    {   String h, m;
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
                        startTime = h + ":" + m;
                        startTimebtn.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                    }
                };

                // int style = AlertDialog.THEME_HOLO_DARK;

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

                timePickerDialog.setTitle("Select Start Time");
                timePickerDialog.show();

            }
        });

        endTimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        epostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDate = startBtn.getText().toString();
                endDate = endBtn.getText().toString();
                boolean res = dbhelp.insertEvent(ename.getText().toString(), edesc.getText().toString(), startDate + " " + startTime, endDate + " " + endTime, id);
                if(!res) {
                    Toast.makeText(v.getContext(), "Error in inserting",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(v.getContext(), "Successfully inserted event",Toast.LENGTH_SHORT).show();
                    Button organizer_show_button = requireActivity().findViewById(R.id.organizer_show_button);
                    organizer_show_button.performClick();
                }
            }
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

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
}


