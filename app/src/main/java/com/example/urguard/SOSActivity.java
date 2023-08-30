package com.example.urguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SOSActivity extends AppCompatActivity {

    Button btn_stop;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
        int originalVolume = audioManager.getStreamVolume(audioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(audioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC), 0);
        mediaPlayer.start();
        btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                audioManager.setStreamVolume(audioManager.STREAM_MUSIC, originalVolume, 0);
                Intent intent = new Intent(SOSActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

    }
}