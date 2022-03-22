package com.example.eventapp.data;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventapp.R;

import java.util.Date;

public class Post_Event_Fragment extends Fragment {

    EditText ename;
    EditText edesc;
    EditText estartdate, eenddate;
    Button epostbutton;
    DBhelper dbhelp;
    int id;

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
        estartdate = (EditText) v.findViewById(R.id.estartdate);
        eenddate = (EditText) v.findViewById(R.id.eenddate);

        epostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean res = dbhelp.insertEvent(ename.getText().toString(), edesc.getText().toString(), estartdate.getText().toString(), eenddate.getText().toString(), id);
                if(!res) {
                    Toast.makeText(v.getContext(), "Error in inserting",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(v.getContext(), "Successfully inserted event",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;

    }

    }


