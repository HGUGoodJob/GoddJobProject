package com.goodjob.singing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MiddleChoiceSong extends AppCompatActivity {
    String sex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_choice_song);
        Button start_bear = (Button)findViewById(R.id.song_bear);
        RadioButton man = (RadioButton) findViewById(R.id.man);
        RadioButton woman = (RadioButton) findViewById(R.id.woman);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        Button back = (Button)findViewById(R.id.backtochoice);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (
                        getApplicationContext(), ChoiceLevel.class);
                startActivity(intent);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.man){
                    sex="man";
                }else {
                    sex="woman";
                }
            }
        });

        start_bear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (
                        getApplicationContext(), PartPracticeBear1.class);
                intent.putExtra("sex", sex);
                startActivity(intent);
            }
        });
    }
}