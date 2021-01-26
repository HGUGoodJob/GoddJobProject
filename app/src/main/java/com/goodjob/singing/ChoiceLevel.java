package com.goodjob.singing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChoiceLevel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_level);

        ImageButton click_start = (ImageButton)findViewById(R.id.levellow);
        click_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (
                        getApplicationContext(), PartPractice.class);
                startActivity(intent);
            }
        });
    }
}