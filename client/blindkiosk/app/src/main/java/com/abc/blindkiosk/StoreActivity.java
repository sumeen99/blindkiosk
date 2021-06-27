package com.abc.blindkiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class StoreActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.KOREAN);
                    findUserLocation();
                }
            }
        });
    }


    void findUserLocation(){
        textToSpeech.speak("사용자의 위치를 탐색중입니다.",TextToSpeech.QUEUE_ADD,null);

        chooseStore();
    }

    void chooseStore(){
        textToSpeech.speak("가게를 선택해주세요.",TextToSpeech.QUEUE_ADD,null);
    }
}