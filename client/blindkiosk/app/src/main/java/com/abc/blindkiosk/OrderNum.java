package com.abc.blindkiosk;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class OrderNum extends AppCompatActivity {


    TextView numText;
    int num = 123;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordernum);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.KOREAN);
                }
            }
        });

        numText = findViewById(R.id.numText);
        numText.setText(Integer.toString(num) + "번");
        textToSpeech.speak("주문번호는 " + Integer.toString(num) + "번 입니다.", TextToSpeech.QUEUE_ADD, null);

    }
}
