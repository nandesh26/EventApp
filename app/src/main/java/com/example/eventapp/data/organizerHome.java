package com.example.eventapp.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventapp.R;

public class organizerHome extends AppCompatActivity {

    // Views ...
    Button organizer_post_button;
    Button organizer_show_button;

    Post_Event_Fragment f1; // First fragment ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home);


        Intent i = getIntent();
        int organizer_id = i.getIntExtra("organizer_id", 0);

        f1 = new Post_Event_Fragment(organizer_id);

        // Finding views by id ...
        organizer_show_button = findViewById(R.id.organizer_show_button);
        organizer_post_button = findViewById(R.id.organizer_post_button);

        // When post button is clicked ...
        organizer_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fragment Handling ...
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.organizer_frame, f1);
                ft.commit();
                Toast.makeText(getApplicationContext(), "New fragment", Toast.LENGTH_SHORT).show();
            }
        });

    }
}