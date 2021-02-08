package com.goodjob.singing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class LowChoiceSong extends AppCompatActivity {
    String sex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_choice_song);
        Button start_plane = (Button)findViewById(R.id.song_plane);
        CheckBox man = (CheckBox) findViewById(R.id.man);
        CheckBox woman = (CheckBox) findViewById(R.id.woman);

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex="man";
            }
        });

        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex="woman";
            }
        });

        start_plane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (
                        getApplicationContext(), PartPracticePlane1.class);
                intent.putExtra("sex", sex);
                startActivity(intent);
            }
        });
    }


}