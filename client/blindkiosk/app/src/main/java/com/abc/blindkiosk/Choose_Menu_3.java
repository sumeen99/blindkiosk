package com.abc.blindkiosk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Choose_Menu_3 extends AppCompatActivity {

    TextView textView;
    Button button_speak;
    Button button_ok;

    Intent intent;
    Intent storeIntent;
    Context context;
    TextToSpeech textToSpeech;
    SpeechRecognizer mRecognizer;
    Number number = new Number();
    String numberinfo;

    String type = "store";
    String storeName;
    String storeId;
    String customId;
    List<String> allFoodArray;
    String menuName;
    JSONObject foodInfo; //카트에 넣을 메뉴 정보를 담는 json - (+icedType)
    ArrayList<CartList> cartList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_menu_2);

        textView = findViewById(R.id.textView);
        button_speak = findViewById(R.id.button_modify);
        button_ok = findViewById(R.id.button_ok);
        storeIntent = getIntent();
        //storeName = storeIntent.getStringExtra("storeName");
        storeName = "공차";
        context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Choose_Menu_3.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

            Toast.makeText(getApplicationContext(), "퍼미션 체크 완료.", Toast.LENGTH_SHORT).show();
        }

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.KOREAN);
                    storeId = getStringId();
                    speakMenu();
                }
            }
        });

    }

    void speakMenu(){

        type = "food";

        allFoodArray = getListId();

        if(storeName=="맥도날드"){
            textToSpeech.speak("세트의 경우 메뉴명을 버거이름을 말하고 끝에 '세트'를 붙여 말해주세요.", TextToSpeech.QUEUE_ADD, null);
        }
        getUserSpeak("상단 버튼을 누르고 원하는 메뉴를 말해주세요.");
    }

    String getStringId() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<String> task = new Callable<String>() {
            @Override
            public String call() {
                if (type.equals("store")) {
                    return MenuAPI.storeName(storeName);
                }
                return MenuAPI.custom(customId);
            }
        };
        Future<String> future = executorService.submit(task);

        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    List<String> getListId() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<List<String>> task = new Callable<List<String>>() {
            @Override
            public List<String> call() {
                return MenuAPI.allMenu(storeId);
            }
        };

        Future<List<String>> future = executorService.submit(task);

        try {
            return future.get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    void getUserSpeak(String guideText) {
        textToSpeech.speak(guideText, TextToSpeech.QUEUE_ADD, null);
        button_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(Choose_Menu_3.this); //
                mRecognizer.setRecognitionListener(listener);   //모든 콜백을 수신하는 리스너 설정
                mRecognizer.startListening(intent);
            }
        });
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(context, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    textToSpeech.speak("설정에서 마이크 권한을 허용해주세요.", TextToSpeech.QUEUE_ADD, null);
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    textToSpeech.speak("잠시 후에 다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    textToSpeech.speak("말하는 시간이 초과되었습니다. 화면 상단을 눌러 다시 시도해주세요.", TextToSpeech.QUEUE_ADD, null);
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(context, "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < matches.size(); i++) {
                textView.setText(matches.get(i));
            }

            if(type.equals("food")){
                menuName = textView.getText().toString();
                textToSpeech.speak("원하는 메뉴가 " + menuName + "이 맞으면 하단 버튼을 누르고 아니라면 상단 버튼을 누르고 메뉴명을 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
            } else if(type.equals("custom")){

            }


            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type.equals("food")){
                        findMenu();
                    }
                }
            });

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    void findMenu(){

        boolean flag = false;
        menuName = menuName.replace(" ", "");

        for(int i = 0; i < allFoodArray.size(); i++){
            try {
                foodInfo = new JSONObject(allFoodArray.get(i));
                String name = foodInfo.getString("name").replace(" ", "");
                if(name.equals(menuName)){
                    type = "custom";
                    flag = true;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!flag){
            getUserSpeak("선택하신 메뉴가 없습니다. 상단 버튼을 누르고 메뉴를 다시 얘기해 주세요.");
        } else{
            goCustom();
        }
    }

    void goCustom(){

    }



}
