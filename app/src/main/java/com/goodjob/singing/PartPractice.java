package com.goodjob.singing;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.PorterDuff;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.io.android.AndroidAudioPlayer;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.writer.WriterProcessor;

public class PartPractice extends AppCompatActivity {
    AudioDispatcher dispatcher;
    TarsosDSPAudioFormat tarsosDSPAudioFormat;  //TarsosDSP Format 세팅

    File file;

    TextView pitchTextView;
    Button pitchButton, pitchbutton2, pitchbutton3, pitchbutton4, pitchbutton5, pitchbutton6, pitchbutton7; //recordButton -> pitchButton
    TextView highPitch;
    TextView lowPitch;
    TextView low;
    ImageView pitchline;
    String filename = "recorded_sound.wav";
    float note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_practice);

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
        pitchButton = findViewById(R.id.pitchButton);
        pitchbutton2 = findViewById(R.id.pitchbutton2);
        pitchbutton3 = findViewById(R.id.pitchbutton3);
        pitchbutton4 = findViewById(R.id.pitchbutton4);
        pitchbutton5 = findViewById(R.id.pitchbutton5);
        pitchbutton6 = findViewById(R.id.pitchbutton6);
        pitchbutton7 = findViewById(R.id.pitchbutton7);
        highPitch = findViewById(R.id.highpitch);
        lowPitch = findViewById(R.id.lowpitch);
        pitchline = findViewById(R.id.pitchline);

        pitchButton.setOnClickListener(new View.OnClickListener() {
            //녹음 버튼을 누르면 녹음 실행

            @Override
            public void onClick(View v) {
                    pitchButton.setTextColor(Color.parseColor("#FF000000"));
                    recordAudio2(330.000f);
            }
        });

        pitchbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio2(294.000f);
            }
        });
        pitchbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio2(262.000f);
            }
        });
        pitchbutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio2(294.000f);
            }
        });
        pitchbutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio2(330.000f);
            }
        });
        pitchbutton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio2(330.000f);
            }
        });
        pitchbutton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio2(330.000f);
            }
        });

    }


    /*public void recordAudio()
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
                    final float mi = 330.000f;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pitchTextView.setText(pitchInHz + "");
                            if(Float.compare(pitchInHz, mi - 20.000f) < 0){
                                pitchline.setColorFilter(null);
                                lowPitch.setTextColor(Color.parseColor("#e65d5d"));
                                highPitch.setTextColor(Color.parseColor("#0a0a0a"));
                            }else if(Float.compare(pitchInHz, mi + 20.000f) > 0){
                                pitchline.setColorFilter(null);
                                lowPitch.setTextColor(Color.parseColor("#0a0a0a"));
                                highPitch.setTextColor(Color.parseColor("#e65d5d"));
                            }else{
                                pitchline.setColorFilter(Color.parseColor("#82fa46"), PorterDuff.Mode.SRC_IN);
                                lowPitch.setTextColor(Color.parseColor("#0a0a0a"));
                                highPitch.setTextColor(Color.parseColor("#0a0a0a"));
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
    }*/

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
                            pitchTextView.setText(pitchInHz + "");
                            if(Float.compare(pitchInHz, note - 5.000f) < 0){
                                pitchline.setColorFilter(null);
                                lowPitch.setTextColor(Color.parseColor("#e65d5d"));
                                highPitch.setTextColor(Color.parseColor("#0a0a0a"));
                            }else if(Float.compare(pitchInHz, note + 5.000f) > 0){
                                pitchline.setColorFilter(null);
                                lowPitch.setTextColor(Color.parseColor("#0a0a0a"));
                                highPitch.setTextColor(Color.parseColor("#e65d5d"));
                            }else{
                                pitchline.setColorFilter(Color.parseColor("#82fa46"), PorterDuff.Mode.SRC_IN);
                                lowPitch.setTextColor(Color.parseColor("#0a0a0a"));
                                highPitch.setTextColor(Color.parseColor("#0a0a0a"));
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