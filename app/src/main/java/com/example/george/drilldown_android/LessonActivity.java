package com.example.george.drilldown_android;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import android.os.Handler;
import android.os.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LessonActivity extends AppCompatActivity {

    private String baseUrl = "http://10.0.2.2:8080";
    private String audioUrl;
    private String imageUrl;
    private String title;


    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView lesson_title;
    TextView remainingTimeLabel;
    ImageView topicPlayerImageView;

    MediaPlayer mp;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        Intent intent = getIntent();
        audioUrl = intent.getStringExtra("audio");
        imageUrl = intent.getStringExtra("image");
        title = intent.getStringExtra("title");

        playBtn = (Button) findViewById(R.id.playBtn);
        topicPlayerImageView = (ImageView) findViewById(R.id.topicPlayerImageView);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        lesson_title = (TextView) findViewById(R.id.lesson_title);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);
        lesson_title.setText(title);
        Picasso.get().load(baseUrl + "/" + imageUrl).into(topicPlayerImageView);

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.i("io",baseUrl + "/" + audioUrl);
        try {
            mp.setDataSource(baseUrl + "/" + audioUrl);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        // Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(onSeekBarChangeListener);


        // Volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(onSeekBarChangeListener2);

        // Thread (Update positionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void playBtnClick(View view) {

        if (!mp.isPlaying()) {
            // Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);

        } else {
            // Playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }

    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mp.seekTo(progress);
                positionBar.setProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener2 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            float volumeNum = progress / 100f;
            mp.setVolume(volumeNum, volumeNum);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
