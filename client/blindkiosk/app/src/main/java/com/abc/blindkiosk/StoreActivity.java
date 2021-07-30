package com.abc.blindkiosk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
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

    Button btnSpeech;
    Button btnOK;

    TextView answer;

    double longitude;
    double latitude;
    LocationManager locationManager;
    Intent intent;
    Context context;
    SpeechRecognizer mRecognizer;

    String answerInfo;
    Number number = new Number();
    Store store = new Store();

    int set = 0;
    List<String> stores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_menu);

        btnSpeech = (Button) findViewById(R.id.button_speak);
        btnOK = (Button) findViewById(R.id.button_payment);
        answer = (TextView) findViewById(R.id.textView);

        intent = getIntent();
        context = getApplicationContext();


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.speak("앱을 사용하면서 선택사항을 다시 듣고싶으실 때는 다시 듣기라고 말해주시고 말을 할때는 상단의 말하기 버튼을 누른 후 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
                    textToSpeech.setLanguage(Locale.KOREAN);
                    intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                    findUserLocation();

                }
            }
        });

    }


    void findUserLocation() {

        Log.d("Chk", "위치찾기!!!!!!!!!!!!!!");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        textToSpeech.speak("사용자의 위치를 탐색중입니다.", TextToSpeech.QUEUE_ADD, null);
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

    void getUserSpeak() {
        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.stop();
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
            if (answer.getText().toString().contains("다시듣기") || answer.getText().toString().contains("다시 듣기")) {
                textToSpeech.speak("가게 목록을 다시 알려드립니다.", TextToSpeech.QUEUE_ADD, null);
                chooseStore();
            } else {
                answerInfo = number.findNumberByCnt(answer.getText().toString(), stores.size());
                Toast.makeText(context, "사용자 답변 확인.", Toast.LENGTH_SHORT).show();
                if (answerInfo == null) {
                    textToSpeech.speak("번호를 인식하지 못하였습니다. 번호를 다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
                } else {
                    textToSpeech.speak("선택하신 번호가 " + answerInfo + "번이 맞으면 화면 하단을 눌러주시고 아니면 다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textToSpeech.stop();
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
                //x=경도, y=위도
                //return storeAPI.access(longitude + "", latitude + "");
                return storeAPI.access(127.07373482196793 + "", 37.547173391279266 + "");

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
        if (set * 5 > stores.size()) {
            set = 0;
            textToSpeech.speak("모든 가게 정보를 보았습니다. 처음으로 돌아갑니다.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak("주변 가게 목록은", TextToSpeech.QUEUE_ADD, null);
        for (int i = set * 5; i < set * 5 + 5; i++) {
            if (i < stores.size()) {
                Log.d("name", stores.get(i));
                textToSpeech.speak(i - set * 5 + 1 + "번" + stores.get(i), TextToSpeech.QUEUE_ADD, null);
            }
        }
        textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
        if (stores.size() <= Constants.TOTAL_SIZE) {
            textToSpeech.speak("원하는 가게 번호를 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
        } else {
            textToSpeech.speak("원하는 가게 번호를 말씀해주시고 없으면 0번을 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
        }
        getUserSpeak();
    }

    void getStoreName() {
        String storeName = stores.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1);
        textToSpeech.speak(storeName + "을 선택하셨습니다.", TextToSpeech.QUEUE_ADD, null);
        Log.d("StoreName", storeName);
        storeName = store.findStore(storeName);
        if (storeName == null) {
            textToSpeech.speak("해당 가게에 대한 정보가 없습니다.", TextToSpeech.QUEUE_ADD, null);
        } else {
            textToSpeech.stop();
            Intent storeNameIntent = new Intent(this, Choose_Menu.class);
            storeNameIntent.putExtra("storeName", storeName);
            startActivity(storeNameIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.stop();
    }
}