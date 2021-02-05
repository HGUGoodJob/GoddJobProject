package com.goodjob.singing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ChoiceLevel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_level);

        Button start_low = (Button)findViewById(R.id.levellow);
        Button start_middle = (Button)findViewById(R.id.levelmiddle);

        start_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent (
                        getApplicationContext(), LowChoiceSong.class);
                startActivity(intent1);
            }
        });

        start_middle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent2 = new Intent (
                        getApplicationContext(), MiddleChoiceSong.class);
                startActivity(intent2);
            }
        });
    }
}