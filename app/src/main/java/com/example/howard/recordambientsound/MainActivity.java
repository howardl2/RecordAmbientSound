package com.example.howard.recordambientsound;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivityTag";
    private static final String PERMISSIONTAG = "permissions";

    private Button startRecordingButton;
    private TextView currTime;
    private TextView prevTime;
    private BroadcastReceiver receiver;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;
    private static final int PERMISSIONS_ALL = 10;

    private String prevTimeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!checkPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_ALL);
        }

        startRecordingButton = (Button) findViewById(R.id.startRecording);
        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initRecordingAudio();
            }
        });
        currTime = (TextView) findViewById(R.id.timeText);
        prevTime = (TextView) findViewById(R.id.prevTimeText);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                prevTime.setText(currTime.getText());
                String s = intent.getStringExtra(RecordingService.TIME_MESSAGE);
                Log.d(TAG, s);
                currTime.setText(s);


            }
        };

    }

    private boolean checkPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RECORD_AUDIO: {
                Log.d(PERMISSIONTAG,"RESULT PERMISSION RECORD AUDIO");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(PERMISSIONTAG,"PERMISSION WAS GRANTED");

                }
            }
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                Log.d(PERMISSIONTAG,"RESULT PERMISSION WRITE EXTERNAL STORAGE");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(PERMISSIONTAG,"PERMISSION WAS GRANTED");

                }
            }
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                Log.d(PERMISSIONTAG,"RESULT PERMISSION READ EXTERNAL STORAGE");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(PERMISSIONTAG,"PERMISSION WAS GRANTED");

                }
            }
            case PERMISSIONS_ALL: {
                Log.d(PERMISSIONTAG,"RESULT PERMISSION ALL");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(PERMISSIONTAG,"PERMISSION WAS GRANTED");

                }
            }
        }
    }

    public int initRecordingAudio() {
        Intent recordingIntent = new Intent(this, RecordingService.class);
        startService(recordingIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(RecordingService.TIME_RESULT)
        );
        return 1;
    }
}
