package com.abc.blindkiosk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class LastOrder extends AppCompatActivity {

    ArrayList<CartList> menuList;

    TextView textView;
    Button button_modify;
    Button button_ok;
    TextToSpeech textToSpeech;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lastorder);

        menuList = (ArrayList<CartList>) getIntent().getSerializableExtra("menuList");
        textView = findViewById(R.id.textView);
        button_ok = findViewById(R.id.button_ok);
        button_modify = findViewById(R.id.button_modify);
        context = getApplicationContext();


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.KOREAN);
                    readCartList();
                }
            }
        });
    }

    void readCartList(){

        int price = 0;

        textToSpeech.speak("선택하신 메뉴들은 다음과 같습니다.", TextToSpeech.QUEUE_ADD, null);
        for(int i = 0; i < menuList.size(); i++){
            CartList cartList = menuList.get(i);
            textToSpeech.speak((i + 1) + " 번" + cartList.name, TextToSpeech.QUEUE_ADD, null);
            if(cartList.size!="null"){
                textToSpeech.speak(cartList.size + "사이즈", TextToSpeech.QUEUE_ADD, null);
            }
            if(cartList.temp!="null"){
                textToSpeech.speak(cartList.temp, TextToSpeech.QUEUE_ADD, null);
            }
            textToSpeech.speak(cartList.quantity + "개", TextToSpeech.QUEUE_ADD, null);
            price += cartList.price;
        }
        textToSpeech.speak("총" + price + "원 입니다.", TextToSpeech.QUEUE_ADD, null);

        textToSpeech.speak("메뉴를 다시 선택하고 싶으면 상단버튼을 누르고 결제를 하시려면 하단 버튼을 눌러주세요.", TextToSpeech.QUEUE_ADD, null);

        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Choose_Menu.class);
                startActivity(intent);
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, payment.class);
                startActivity(intent);
            }
        });
    }

}
