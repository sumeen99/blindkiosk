package com.abc.blindkiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Choose_Menu extends AppCompatActivity {

    Button btn;
    TextView textView;
    STT stt;
    TTS tts;
    String num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);

        TTS tts1 = new TTS("메뉴 선택 방식을 선택해주세요");
        TTS tts2 = new TTS("1번. 전체 메뉴 읽기");
        TTS tts3 = new TTS("2번. 카테고리별 메뉴 선택하기");
        TTS tts4 = new TTS("3번. 바로 메뉴 선택하기");

        textView = findViewById(R.id.textview);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stt = new STT(btn, textView);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        num = stt.answer.getText().toString();
        if(num == "2번"){
            Intent intent = new Intent(this, Choose_Menu_2.class);
            startActivity(intent);
        } else if(num == "3번"){
            Intent intent = new Intent(this, Choose_Menu_3.class);
            startActivity(intent);
        } else{
            TTS tts = new TTS("잘못된 대답입니다. 버튼을 누르고 다시 답해주세요");
        }
    }


}
