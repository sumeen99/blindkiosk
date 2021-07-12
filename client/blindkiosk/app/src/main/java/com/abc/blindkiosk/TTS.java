package com.abc.blindkiosk;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class TTS {

    private TextToSpeech tts;
    TextView textView;
    Context context;

    TTS (TextView textView, Context context) {

        this.textView = textView;
        this.context = context;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });


    }
    void speakToUser(String guide){
        tts.speak(guide, TextToSpeech.QUEUE_FLUSH, null);
    }

}
