package com.example.eventapp.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.data.Database.Organizer;

import java.util.List;

public class organizerHome extends AppCompatActivity {

    // Views ...
    Button organizer_post_button;
    Button organizer_show_button, organizer_scan_button;

    Post_Event_Fragment f1; // First fragment ...
    Show_Events_List_Fragment f2;

    Organizer organizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home);


        Intent i = getIntent();
//        int organizer_id = Integer.parseInt(i.getStringExtra("organizer_id"));
        organizer = i.getParcelableExtra("organizer");

        f1 = new Post_Event_Fragment(organizer.getUsername());
        f2 = new Show_Events_List_Fragment(organizer.getUsername(), getApplicationContext());

        // Finding views by id ...
        organizer_show_button = findViewById(R.id.organizer_show_button);
        organizer_post_button = findViewById(R.id.organizer_post_button);
        organizer_scan_button = findViewById(R.id.organizer_scanner_button);

        // When post button is clicked ...
        organizer_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fragment Handling ...
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.organizer_frame, f1);
                ft.commit();
                Toast.makeText(getApplicationContext(), "New fragment for posting event by organizer", Toast.LENGTH_SHORT).show();
            }
        });

        organizer_show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.organizer_frame, f2);
                Bundle bd = new Bundle();
                bd.putString("pushed-by", "organizer");
                f2.setArguments(bd);
                ft.commit();
                Toast.makeText(getApplicationContext(), "New fragment for showing events for organizer", Toast.LENGTH_SHORT).show();
            }
        });

        organizer_scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                startActivity(intent);
            }
        });

    }
}