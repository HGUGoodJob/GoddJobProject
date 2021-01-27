package com.goodjob.singing;

import android.content.Intent;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
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
    //TarsosDSPAudioFormat class-> 채널 수, 샘플 속도, 샘플 크기, 바이트 순서, 프레임 속도 및 프레임 크기 등과
    // 오디오 인코딩 형식 등을 설정하기 위한 클래스

    File file;

    TextView pitchTextView; //주파수(?)를 보여주는 뷰
    Button recordButton; //녹음버튼
    Button playButton; //재생버튼
    boolean isRecording = false;  //녹음상태
    String filename = "recorded_sound.wav";  //녹음과 재생 할 파일의 경로 지정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_practice);  //activity_part_practice.xml 실

        File sdCard = Environment.getExternalStorageDirectory(); //녹음과 재생 할 파일의 경로 지정
        file = new File(sdCard, filename);  //녹음과 재생 할 파일의 경로 지정
        /*
        filePath = file.getAbsolutePath();
        Log.e("MainActivity", "저장 파일 경로 :" + filePath); // 저장 파일 경로 : /storage/emulated/0/recorded.mp4
        */

        tarsosDSPAudioFormat=new TarsosDSPAudioFormat(TarsosDSPAudioFormat.Encoding.PCM_SIGNED,
                22050,
                2 * 8,
                1,
                2 * 1,
                22050,
                ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder()));
//        Constructor parameters
//        - encoding - the audio encoding technique
//        - sampleRate - the number of samples per second
//        - sampleSizeInBits - the number of bits in each sample
//        - channels - the number of channels (1 for mono, 2 for stereo, and so on)
//        - frameSize - the number of bytes in each frame
//        - frameRate - the number of frames per second
//        - bigEndian - indicates whether the data for a single sample is stored in big-endian byte order (false means little-endian)
//        Encording Technique
//        - TarsosDSPAudioFormat.Encoding.ALAW - Specifies a-law encoded data.
//        - TarsosDSPAudioFormat.Encoding.PCM_SIGNED - Specifies signed, linear PCM data.
//        - TarsosDSPAudioFormat.Encoding.PCM_UNSIGNED - Specifies unsigned, linear PCM data.
//        - TarsosDSPAudioFormat.Encoding.ULAW - Specifies u-law encoded data.

        pitchTextView = findViewById(R.id.pitchTextView);  //xml 파일과 연계하여 id가 pitchTextView인 뷰를 가져옴
        recordButton = findViewById(R.id.recordButton); //위의 설명과 같습니다
        playButton = findViewById(R.id.playButton);  //위의 설명과 같습니다

        recordButton.setOnClickListener(new View.OnClickListener() {
            //녹음 버튼을 누르면 녹음 실행
            @Override
            public void onClick(View v) {
                if(!isRecording)
                {
                    recordAudio();  //아래에 함수 있음
                    isRecording = true;
                    recordButton.setText("중지"); // 녹음 시작하면 버튼에 글자가 중지로 바뀜
                }
                else
                {
                    stopRecording();
                    isRecording = false;
                    recordButton.setText("녹음");
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });
    }

    public void playAudio()
            //저장된 사운드 파일을 실행하면서 실시간 pitch Detection 수
    {
        try{
            releaseDispatcher();

            FileInputStream fileInputStream = new FileInputStream(file);
            dispatcher = new AudioDispatcher(new UniversalAudioInputStream(fileInputStream, tarsosDSPAudioFormat), 1024, 0);
                    //마이크가 아닌 파일을 소스로 하는 dispatcher 생성

            AudioProcessor playerProcessor = new AndroidAudioPlayer(tarsosDSPAudioFormat, 2048, 0);
            //오디오 플레이를 위한 AudioProcessor
            dispatcher.addAudioProcessor(playerProcessor);

            PitchDetectionHandler pitchDetectionHandler = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult res, AudioEvent e){
                    final float pitchInHz = res.getPitch();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pitchTextView.setText(pitchInHz + "");
                        }
                    });
                }
            }; //pitch detection 은 녹음할 때와 동일

            AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pitchDetectionHandler);
            dispatcher.addAudioProcessor(pitchProcessor);

            Thread audioThread = new Thread(dispatcher, "Audio Thread");
            audioThread.start();

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void recordAudio()
    {
        releaseDispatcher();
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
                    //녹음을 위한 파일 생성
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

    public void stopRecording()
    {
        releaseDispatcher();
    } //녹음 중지

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