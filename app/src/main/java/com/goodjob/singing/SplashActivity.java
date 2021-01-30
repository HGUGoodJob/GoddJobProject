package com.goodjob.singing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.VideoView;

public class SplashActivity extends AppCompatActivity {
    //VideoView v;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //상태바 없애는 코
        setContentView(R.layout.activity_splash);



        //v = findViewById(R.id.splash);
        tv = findViewById(R.id.intro);
        Animation ani = AnimationUtils.loadAnimation(this, R.anim.intro);

        tv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.intro));


//        Uri videoUri;
//        videoUri = Uri.parse("android.resource://"+getPackageName()+ "/" + R.raw.mainsplash);
        //v.setVideoURI(videoUri);

//        v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                v.start();
//            }
//        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}