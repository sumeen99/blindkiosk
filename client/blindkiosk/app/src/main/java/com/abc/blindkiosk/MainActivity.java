package com.abc.blindkiosk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Context cThis;
    String LogTT = "[STT]";

    Intent SttIntent;
    SpeechRecognizer mRecognizer;

    TextToSpeech tts;

    Button btnSttStart;
    EditText txtInMsg;
    EditText txtSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cThis = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //음성인식
        SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(cThis);
        mRecognizer.setRecognitionListener(listener);

        //음성출력 생성, 리스너 초기화
        tts = new TextToSpeech(cThis, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        //버튼설정
        btnSttStart = (Button)findViewById(R.id.btn_stt_start);
        btnSttStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("음성인식 시작");
                if(ContextCompat.checkSelfPermission(cThis, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                    //권한을 허용하지 않은 경우
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }
                else{
                    //권한을 허용한 경우
                    try{
                        mRecognizer.startListening(SttIntent);
                    }catch (SecurityException e){e.printStackTrace();}
                }
            }
        });

        txtInMsg = (EditText)findViewById(R.id.txtInMsg);
        txtSystem = (EditText)findViewById(R.id.txtSystem);

        /*new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtSystem.setText("어플 실행 == 자동 실행======="+"\r\n"+txtSystem.getText());
                btnSttStart.performClick();
            }
        },1000);
        */
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            txtSystem.setText("onReadyForSpeech........"+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onBeginningOfSpeech() {
            Toast.makeText(getApplicationContext(), "음성인식 시작", Toast.LENGTH_SHORT).show();
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
            txtSystem.setText("천천히 다시 말해 주세요......."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            txtInMsg.setText(rs[0]+"\r\n"+txtInMsg.getText());
            FuncVoiceOrderCheck(rs[0]);
            mRecognizer.startListening(SttIntent);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    private void FuncVoiceOrderCheck(String VoiceMsg) {
      if(VoiceMsg.length()<1) return;

      VoiceMsg=VoiceMsg.replace(" ","");

      /*if(VoiceMsg.indexOf("카카오톡")>-1 || VoiceMsg.indexOf("카톡")>-1){
          Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.kakao.talk");
          startActivity(launchIntent);
          onDestroy();
      }*/

    }

    private void FuncVoiceOut(String OutMsg){
        if(OutMsg.length()<1) return;

        tts.setPitch(1.0f);
        tts.setSpeechRate(1.0f);
        tts.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts!=null){
            tts.stop();
            tts.shutdown();
            tts=null;
        }
        if(mRecognizer != null){
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer=null;
        }
    }

}