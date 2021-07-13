package com.abc.blindkiosk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StoreActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;
    TextView position;

    Button btnSpeech;
    Button btnOK;

    TextView answer;

    double longitude;
    double latitude;
    LocationManager locationManager;
    //STT stt;
    Intent intent;
    Context context;
    SpeechRecognizer mRecognizer;

    String answerInfo;
    Number number = new Number();

    int set = 0;
    List<String> stores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        btnSpeech = (Button) findViewById(R.id.btnSpeech);
        btnOK = (Button) findViewById(R.id.btnOK);
        position = (TextView) findViewById(R.id.TextViewLocation);
        answer = (TextView) findViewById(R.id.TextViewAnswer);

        intent = getIntent();
        context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StoreActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(StoreActivity.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeech.setLanguage(Locale.KOREAN);
                        findUserLocation();
                    }
                }
            });
            Toast.makeText(getApplicationContext(), "퍼미션 체크 완료.", Toast.LENGTH_SHORT).show();

        }
    }


    void findUserLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        textToSpeech.speak("사용자의 위치를 탐색합니다.", TextToSpeech.QUEUE_ADD, null);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
        } catch (SecurityException ex) {
            Toast.makeText(getApplicationContext(), "탐색 실패", Toast.LENGTH_SHORT).show();
        }

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            position.setText("위도 : " + latitude + "\n" + "경도 : " + longitude);
            //Toast.makeText(getApplicationContext(), "위도 : " + latitude + "\n" + "경도 : " + longitude , Toast.LENGTH_SHORT).show();
            textToSpeech.speak("사용자 위치를 탐색 완료하였습니다.", TextToSpeech.QUEUE_ADD, null);
            locationManager.removeUpdates(mLocationListener);
            chooseStore();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    };

    void getUserSpeak(String guideText) {
        textToSpeech.speak(guideText, TextToSpeech.QUEUE_ADD, null);
        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(StoreActivity.this); //
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
                answer.setText(matches.get(i));
            }

            answerInfo = number.findNumber(answer.getText().toString());
            Toast.makeText(context, "사용자 답변 확인.", Toast.LENGTH_SHORT).show();
            if (answerInfo == null) {
                textToSpeech.speak("번호를 인식하지 못하였습니다. 화면 상단을 눌러 번호를 다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
            } else {
                textToSpeech.speak("선택하신 번호가 " + answerInfo + "번이 맞으면 화면 하단을 눌러주시고 아니면 화면 상단을 눌러 다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answer.setText(answerInfo);
                        if (Integer.parseInt(answer.getText().toString()) == 0) {
                            textToSpeech.speak("가게 목록을 새로 알려드립니다.", TextToSpeech.QUEUE_ADD, null);
                            set += 1;
                            chooseStore();

                        } else {
                            getStoreName();
                        }
                        Toast.makeText(context, "다음 단계", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }


        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };


    List<String> getStoreList() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<List<String>> task = new Callable<List<String>>() {
            @Override
            public List<String> call() {
                StoreAPI storeAPI = new StoreAPI();
                return storeAPI.access(127.07307033455444 + "", 37.547275328253214 + "");

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

    void chooseStore() {
        stores = getStoreList();
        if (stores == null) {
            textToSpeech.speak("주변 가게가 없습니다.", TextToSpeech.QUEUE_ADD, null);
            return;
        }
        if(set*5>stores.size()){
            set = 0;
        }
        textToSpeech.speak("주변 가게 목록은", TextToSpeech.QUEUE_ADD, null);
        for (int i = set * 5; i < set * 5 + 5; i++) {
            if (i < stores.size()) {
                Log.d("name", stores.get(i));
                textToSpeech.speak(i - set * 5 + 1 + "번" + stores.get(i), TextToSpeech.QUEUE_ADD, null);
            }
        }
        textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
        getUserSpeak("화면 상단을 눌러 가게 번호를 말씀해주시고 원하는 가게가 없으면 0번을 말씀해주세요.");

    }

    void getStoreName() {
        String storeName = stores.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1);
        Log.d("StoreName", storeName);
        textToSpeech.speak(storeName + "을 선택하셨습니다.", TextToSpeech.QUEUE_ADD, null);
        Intent storeNameIntent = new Intent(this,Choose_Menu.class);
        storeNameIntent.putExtra("storeName","맥도날드");
        startActivity(storeNameIntent);
    }
}