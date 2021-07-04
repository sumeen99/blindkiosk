package com.abc.blindkiosk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class StoreActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;
    TextView position;

    Button btnSpeech;
    Button btnOK;
    String storeNumber;

    TextView answer;

    STT stt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        btnSpeech = (Button) findViewById(R.id.btnSpeech);
        btnOK = (Button)findViewById(R.id.btnOK);
        position = (TextView) findViewById(R.id.viewPosition);
        answer = (TextView) findViewById((R.id.answer));


        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StoreActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        } else {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeech.setLanguage(Locale.KOREAN);
                        findUserLocation();
                    }
                }
            });
            stt = new STT(btnSpeech,btnOK,answer,getIntent(),getApplicationContext(), StoreActivity.this,textToSpeech,Mode.NUMBER_MODE,storeNumber);
            Toast.makeText(getApplicationContext(), "퍼미션 체크 완료.", Toast.LENGTH_SHORT).show();
        }

    }


    void findUserLocation() {
        textToSpeech.speak("사용자의 위치를 탐색합니다.", TextToSpeech.QUEUE_ADD, null);
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);

            }catch (SecurityException ex){
                Toast.makeText(getApplicationContext(), "탐색 실패", Toast.LENGTH_SHORT).show();
            }

    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            position.setText("위도 : " + latitude + "\n" + "경도 : " + longitude);
            Toast.makeText(getApplicationContext(), "위도 : " + latitude + "\n" + "경도 : " + longitude , Toast.LENGTH_SHORT).show();
            textToSpeech.speak("사용자 위치를 탐색 완료하였습니다.", TextToSpeech.QUEUE_ADD, null);
            chooseStore();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    };


    void chooseStore(){


        stt.getUserSpeak("화면 상단을 눌러 가게 번호를 말씀해주세요.");

    }
}