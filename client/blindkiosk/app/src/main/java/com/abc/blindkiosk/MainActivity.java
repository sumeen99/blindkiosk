package com.abc.blindkiosk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    TextToSpeech textToSpeech;
    Context context;
    List permissionList;
    private final int MULTIPLE_PERMISSIONS = 1023;

    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        intent = new Intent(this, StoreActivity.class);
        Button btnStore = (Button) findViewById(R.id.btnStore);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.KOREAN);
                    if(getPermission()){
                        textToSpeech.speak("화면을 눌러주세요.", TextToSpeech.QUEUE_ADD, null);
                    }

                }
            }
        });
        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {
                    textToSpeech.stop();
                    startActivity(intent);
                }else{
                    getPermission();
                }
            }
        });
    }


    boolean checkPermission() {
        int result;
        permissionList = new ArrayList<>();

        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(context, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }

        if (!permissionList.isEmpty()) {
            return false;
        }
        return true;
    }

    public void requestPermission() {
        textToSpeech.speak("우측 가운데 허용 버튼을 두번 눌러 위치, 마이크 권한을 허용해주세요. 모든 권한을 허용하셨으면 화면을 눌러주세요.", TextToSpeech.QUEUE_ADD, null);
        ActivityCompat.requestPermissions(MainActivity.this, (String[]) permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);

    }

    boolean permissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS && (grantResults.length > 0)) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!permissionResult(requestCode, permissions, grantResults)) {
            requestPermission();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.stop();
    }
}