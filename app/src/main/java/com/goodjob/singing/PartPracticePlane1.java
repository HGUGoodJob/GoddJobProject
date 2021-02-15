package com.goodjob.singing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.writer.WriterProcessor;

public class PartPracticePlane1 extends AppCompatActivity {
    AudioDispatcher dispatcher;
    TarsosDSPAudioFormat tarsosDSPAudioFormat;  //TarsosDSP Format 세팅

    File file;

    TextView pitchTextView;
    Button pitchButton1, pitchbutton2, pitchbutton3, pitchbutton4, pitchbutton5, pitchbutton6, pitchbutton7 ; //recordButton -> pitchButton
    Button playVib;
    Button backtochoice;
    ImageButton next;
    TextView highPitch;
    TextView lowPitch;
    ImageView pitchline;
    String filename = "recorded_sound.wav";
    float note;
    float C;
    float D;
    float E;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_practice_plane1);

        Intent intent = getIntent();
        String sex = intent.getStringExtra("sex");

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        File sdCard = Environment.getExternalStorageDirectory();
        file = new File(sdCard, filename);

        tarsosDSPAudioFormat=new TarsosDSPAudioFormat(TarsosDSPAudioFormat.Encoding.PCM_SIGNED,
                22050,
                2 * 8,
                1,
                2 * 1,
                22050,
                ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder()));

        pitchTextView = findViewById(R.id.pitchTextView);
        pitchButton1 = findViewById(R.id.pitchButton1);
        pitchbutton2 = findViewById(R.id.pitchbutton2);
        pitchbutton3 = findViewById(R.id.pitchbutton3);
        pitchbutton4 = findViewById(R.id.pitchbutton4);
        pitchbutton5 = findViewById(R.id.pitchbutton5);
        pitchbutton6 = findViewById(R.id.pitchbutton6);
        pitchbutton7 = findViewById(R.id.pitchbutton7);
        highPitch = findViewById(R.id.highpitch);
        lowPitch = findViewById(R.id.lowpitch);
        pitchline = findViewById(R.id.pitchline);
        next = findViewById(R.id.next);
        playVib = findViewById(R.id.playVib);
        backtochoice = findViewById(R.id.backtochoice);

        backtochoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (
                        getApplicationContext(), LowChoiceSong.class);
                startActivity(intent);
            }
        });

        playVib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // long[] pattern = {750, 250, 500, 500, 500, 500, 1000};
                long[] pattern = {100, 100, 650, 100, 150, 100, 400, 100, 400, 100, 400, 100, 400, 100, 400 };
                //짝수 인덱스 : 대기시간 . 홀수 인덱스 : 진동시간

                vibrator.vibrate(pattern, -1); // -1은 반복없음. 0은 무한반복
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (
                        getApplicationContext(), PartPracticePlane2.class);
                intent.putExtra("sex", sex);
                startActivity(intent);
            }
        });

        if (sex.equals("man")) { //남성음역대 (옥타브3)
            C = 131.000f; // 도
            D = 147.000f; // 레
            E = 165.000f; // 미
        }

        else { //여성음역대 or 선택하지 않음 (옥타브4)
            C = 262.000f;
            D = 294.000f;
            E = 330.000f;
        }

        pitchButton1.setOnClickListener(new View.OnClickListener() {
            //녹음 버튼을 누르면 녹음 실행

            @Override
            public void onClick(View v) {
                buttonColored();
                pitchButton1.setSelected(true);
                recordAudio2(E); //미
            }
        });

        pitchbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonColored();
                pitchbutton2.setSelected(true);
                recordAudio2(D); //레
            }
        });
        pitchbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonColored();
                pitchbutton3.setSelected(true);
                recordAudio2(C); //도
            }
        });
        pitchbutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonColored();
                pitchbutton4.setSelected(true);
                recordAudio2(D); //레
            }
        });
        pitchbutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonColored();
                pitchbutton5.setSelected(true);
                recordAudio2(E); //미
            }
        });
        pitchbutton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonColored();
                pitchbutton6.setSelected(true);
                recordAudio2(E); //미
            }
        });
        pitchbutton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonColored();
                pitchbutton7.setSelected(true);
                recordAudio2(E); //미
            }
        });

    }

    public void recordAudio2(float note)
    {
        releaseDispatcher();
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
            AudioProcessor recordProcessor = new WriterProcessor(tarsosDSPAudioFormat, randomAccessFile);
            dispatcher.addAudioProcessor(recordProcessor);

            PitchDetectionHandler pitchDetectionHandler = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult res, AudioEvent e){
                    final float pitchInHz = res.getPitch();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //pitchTextView.setText(pitchInHz + "");
                            if(Float.compare(pitchInHz, note - 5.000f) < 0){
                                pitchline.setColorFilter(null);
                                lowPitch.setTextColor(Color.parseColor("#e65d5d"));
                                highPitch.setTextColor(Color.parseColor("#0a0a0a"));
                                pitchTextView.setText("");
                            }else if(Float.compare(pitchInHz, note + 5.000f) > 0){
                                pitchline.setColorFilter(null);
                                lowPitch.setTextColor(Color.parseColor("#0a0a0a"));
                                highPitch.setTextColor(Color.parseColor("#e65d5d"));
                                pitchTextView.setText("");
                            }else{
                                pitchline.setColorFilter(Color.parseColor("#82fa46"), PorterDuff.Mode.SRC_IN);
                                lowPitch.setTextColor(Color.parseColor("#0a0a0a"));
                                highPitch.setTextColor(Color.parseColor("#0a0a0a"));
                                pitchTextView.setText("정확해요!");
                            }
                        }
                    });
                }
            };
            AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pitchDetectionHandler);
            dispatcher.addAudioProcessor(pitchProcessor);

            Thread audioThread = new Thread(dispatcher, "Audio Thread");
            audioThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buttonColored(){
        pitchButton1.setSelected(false);
        pitchbutton2.setSelected(false);
        pitchbutton3.setSelected(false);
        pitchbutton4.setSelected(false);
        pitchbutton5.setSelected(false);
        pitchbutton6.setSelected(false);
        pitchbutton7.setSelected(false);
    }
    public void releaseDispatcher()
    {
        if(dispatcher != null)
        {
            if(!dispatcher.isStopped())
                dispatcher.stop();
            dispatcher = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseDispatcher();
    }
}