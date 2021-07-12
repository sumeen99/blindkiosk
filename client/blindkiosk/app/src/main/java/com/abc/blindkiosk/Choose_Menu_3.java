package com.abc.blindkiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Choose_Menu_3 extends AppCompatActivity {

    Button btn;
    TextView textView;
    //STT stt;
    String menu;
    Intent intent;
    List<List<String>> cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);

        textView = findViewById(R.id.textview);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stt = new STT(btn, textView);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*while(true){
            int menu_num = 0;
            TTS tts = new TTS("버튼을 누른 후 메뉴명을 말해주세요", this);
            menu = stt.matches.get(0);

            //메뉴 명 비교하고 menu 이름 확정짓는 코드 만들어야됨.

            while(true){
                //TTS tts_num = new TTS("버튼을 누른 후 수량을 말해 주세요", this);
                String num = stt.matches.get(0);
                if(num == "한 개" || num == "일" || num == "하나"){
                    menu_num = 1;
                    break;
                } else if(num == "두 개"||num == "이"||num == "둘"){
                    menu_num = 2;
                    break;
                } else if(num == "세 개"||num == "삼"||num == "셋"){
                    menu_num = 3;
                    break;
                }else if(num == "네 개"||num == "사"||num == "넷"){
                    menu_num = 4;
                    break;
                }else if(num == "다섯 개"||num == "오"||num == "다섯"){
                    menu_num = 5;
                    break;
                } else{
                    TTS error = new TTS("선택 가능한 수량을 넘었거나 숫자를 정확히 인식하지 못하였습니다.", this);
                }
            }
            cart = new ArrayList<>();
            List<String> Menu = new ArrayList<>();
            Menu.add(menu);
            Menu.add(Integer.toString(menu_num));
            cart.add(Menu);

            TTS contin = new TTS("버튼을 누르고 주문을 계속하시려면 예 를, 아니라면 아니요 를 말해주세요.", this);
            if(stt.matches.get(0) == "예"){
                continue;
            }else{
                break;
            }
        }

        intent = new Intent(getApplicationContext(), LastOrder.class);
        intent.putExtra("MenuList", (Serializable) cart);
        startActivity(intent);
        */

    }


}
