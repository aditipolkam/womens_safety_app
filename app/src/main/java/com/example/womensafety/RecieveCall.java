package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

public class RecieveCall extends AppCompatActivity {

    private ImageView attendCall, endCall;
    private Vibrator vibrator;
    private Ringtone ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_call);
        playRingtone();
        attendCall=(ImageView)findViewById(R.id.attend_call);
        endCall=(ImageView)findViewById(R.id.end_call);

        attendCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringtone.isPlaying())
                {
                    ringtone.stop();
                }
                vibrator.cancel();
                Intent intent1 = new Intent(RecieveCall.this, VoiceCallActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringtone.isPlaying())
                {
                    ringtone.stop();
                }
                vibrator.cancel();
                finish();
            }
        });
    }
    public Ringtone playRingtone()
    {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(20000);
        ringtone.play();
        return ringtone;
    }
}
