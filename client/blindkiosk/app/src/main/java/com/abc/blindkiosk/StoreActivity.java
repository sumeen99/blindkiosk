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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class StoreActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;
    Button btnGPS;
    TextView position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        position = (TextView)findViewById(R.id.position);
        btnGPS = (Button) findViewById(R.id.btnGPS);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.KOREAN);
                    findUserLocation(getApplicationContext());
                }
            }
        });

    }


    void findUserLocation(final Context context) {
        textToSpeech.speak("사용자의 위치를 탐색합니다.화면을 눌러주세요.", TextToSpeech.QUEUE_ADD, null);


        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1,mLocationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1,mLocationListener);
                }catch (SecurityException ex){
                    locationManager.removeUpdates(mLocationListener);
                }

            }
        });


        //chooseStore();
    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            position.setText("위치정보 : " + provider + "\n" + "위도 : " + latitude + "\n" + "경도 : " + longitude + "\n" + "고도 : " + altitude);
        }
    };
    void chooseStore(){
        textToSpeech.speak("가게를 선택해주세요.",TextToSpeech.QUEUE_ADD,null);
    }
}