package com.abc.blindkiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class Choose_Menu_2 extends AppCompatActivity {

    TextView textView;
    Button btn;
    TTS tts;
    STT stt;
    ArrayList<String> answer;

    //메뉴
    ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("햄버거","사이드","음료"));
    String[] hamburger = {"치즈버거", "불고기버거", "치킨버거"};
    String[][] hamburger_inner = new String[][] {
            {"빵","양상추","피클","소스","치즈"},// hbg_inner[0][0], hbg_inner[0][1]
            {"빵","양상추","피클","소스","불고기"},// hbg_inner[1][0], hbg_inner[1][1]
            {"빵","양상추","피클","소스", "치킨"}// hbg_inner[2][0], hbg_inner[2][1]
    }; //햄버거 - 빵, 양상추, 치즈, 피클, 치킨, 불고기, 소스




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);

        textView = findViewById(R.id.textview);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stt = new STT(btn, textView);
            }
        });

        tts = new TTS(textView,getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();

        tts.speakToUser("카테고리를 선택해 주세요.");

        int flag;
        while(true){
            flag = choose_category();
            if(flag != -1){

                break;
            } else{
                tts.speakToUser("카테고리를 다시 선택해주세요.");
            }
        }



    }

    int choose_category() {

        stt = new STT(btn, textView);
        answer = stt.matches;
        int flag = -1;

        for (int i = 0; i < categoryList.size(); i++){
            boolean contain = answer.contains(categoryList.get(i));
            if (contain){
                flag = i;
                break;
            }
        }

        return flag;
    }

    void choose_menu(int category_num) {

        stt = new STT(btn, textView);
        answer = stt.matches;



    }
}
