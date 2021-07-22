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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
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
    String customArray;
    List<String> allFoodArray;
    String menuName;
    JSONObject foodInfo; //카트에 넣을 메뉴 정보를 담는 json - (+iceType)
    ArrayList<CartList> cartList = new ArrayList<>();

    int set = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_menu);

        textView = findViewById(R.id.textView);
        button_speak = findViewById(R.id.button_speak);
        button_ok = findViewById(R.id.button_payment);
        storeIntent = getIntent();
        storeName = storeIntent.getStringExtra("storeName");
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

    void speakMenu() {

        type = "food";

        allFoodArray = getListId();

        if (storeName == "맥도날드") {
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

            if (type.equals("food")) {
                menuName = textView.getText().toString();
                textToSpeech.speak("원하는 메뉴가 " + menuName + "가 맞으면 하단 버튼을 누르고 아니라면 상단 버튼을 누르고 메뉴명을 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
            } else {
                numberinfo = number.findNumber(textView.getText().toString());
                if (numberinfo != null) {
                    if (numberinfo.equals("0")) { //리스트 5개 다시 불러주는 경우
                        set += 1;
                        switch (type) {
                            case "topping":
                                chooseTopping();
                                break;
                            case "finalMenu":
                                textToSpeech.speak("주문을 마칩니다.", TextToSpeech.QUEUE_ADD, null);
                                goLastOrder();
                        }
                    } else { //사용자 말한게 0이 아닌 다른경우
                        switch (type) {
                            case "size":
                                try {
                                    JSONArray jsonArray = foodInfo.getJSONArray("size");
                                    ArrayList<String> sizeArray = new ArrayList<>();
                                    for(int i = 0; i < jsonArray.length(); i++){
                                        sizeArray.add(jsonArray.getString(i));
                                    }
                                    jsonArray = foodInfo.getJSONArray("price");
                                    ArrayList<String> priceArray = new ArrayList<>();
                                    for(int i = 0; i < jsonArray.length(); i++){
                                        priceArray.add(jsonArray.getString(i));
                                    }
                                    foodInfo.put("size", sizeArray.get(Integer.parseInt(numberinfo) - 1));
                                    foodInfo.put("price", priceArray.get(Integer.parseInt(numberinfo) - 1));
                                    textToSpeech.speak("선택하신 사이즈가 " + sizeArray.get(Integer.parseInt(numberinfo) - 1) + "사이즈가 맞으면 하단 버튼을 눌러주시고 아니면 상단 버튼을 누르고 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "topping":
                                try {
                                    JSONObject object = new JSONObject(customArray);
                                    JSONArray jsonArray = object.getJSONArray("type");
                                    ArrayList<String> toppingType = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        toppingType.add(jsonArray.getString(i));
                                    }
                                    jsonArray = object.getJSONArray("price");
                                    ArrayList<String> toppingPrice = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        toppingPrice.add(jsonArray.getString(i));
                                    }

                                    foodInfo.put("toppingType", toppingType.get(set * 5 + Integer.parseInt(numberinfo) - 1));
                                    foodInfo.put("toppingPrice", Integer.parseInt(toppingPrice.get(set * 5 + Integer.parseInt(numberinfo) - 1)));
                                    textToSpeech.speak(toppingType.get(set * 5 + Integer.parseInt(numberinfo) - 1) + " 토핑을 선택한 것이 맞으면 하단 버튼을 누르고 아니면 상단 버튼을 누르고 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "temp":
                                if (numberinfo.equals("1")) {
                                    try {
                                        foodInfo.put("temp", "ice");
                                        textToSpeech.speak("아이스를 선택하셨습니다. 맞으면 하단 버튼을 누르고 아니면 상단 버튼을 누르고 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (numberinfo.equals("2")) {
                                    try {
                                        foodInfo.put("temp", "hot");
                                        textToSpeech.speak("핫을 선택하셨습니다. 맞으면 하단 버튼을 누르고 아니면 상단 버튼을 누르고 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    textToSpeech.speak("잘못된 대답입니다. 상단 버튼을 누르고 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                }
                                break;
                            case "iceAmount":
                                try {
                                    JSONObject object = new JSONObject(customArray);
                                    JSONArray jsonArray = object.getJSONArray("type");
                                    ArrayList<String> iceList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        iceList.add(jsonArray.getString(i));
                                    }
                                    String iceType = iceList.get(Integer.parseInt(numberinfo) - 1);
                                    foodInfo.put("iceType", iceType);
                                    textToSpeech.speak("선택하신 얼음량은 " + iceType + " 입니다.", TextToSpeech.QUEUE_ADD, null);
                                    textToSpeech.speak("맞으면 하단 버튼을 누르고 다시 선택하고 싶으면 상단 버튼을 누르고 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "sweet":
                                try {
                                    JSONObject object = new JSONObject(customArray);
                                    JSONArray jsonArray = object.getJSONArray("type");
                                    ArrayList<String> sweetList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        sweetList.add(jsonArray.getString(i));
                                    }
                                    String sweetType = sweetList.get(Integer.parseInt(numberinfo) - 1);
                                    foodInfo.put("sweetType", sweetType);
                                    textToSpeech.speak("선택하신 당도는 " + sweetType + " 입니다.", TextToSpeech.QUEUE_ADD, null);
                                    textToSpeech.speak("맞으면 하단 버튼을 누르고 다시 선택하고 싶으면 상단 버튼을 누르고 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "quantity":
                                try {
                                    foodInfo.put("quantity", Integer.parseInt(numberinfo));
                                    textToSpeech.speak(numberinfo + " 개 선택하셨습니다. 맞으면 하단 버튼을 누르고 아니면 상단 버튼을 누르고 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "finalMenu":
                                textToSpeech.speak("주문을 계속합니다.", TextToSpeech.QUEUE_ADD, null);
                                speakMenu();
                        }
                    }
                } else {
                    textToSpeech.speak("잘못된 답변입니다. 상단 버튼을 누르고 다시 말씀해 주세요.", TextToSpeech.QUEUE_ADD, null);
                }
            }


            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    set = 0;
                    switch (type) {
                        case "food":
                            findMenu();
                            break;
                        case "size":
                            try {
                                if (foodInfo.getString("customId").equals("null")) {
                                    goQuantity();
                                } else {
                                    chooseTopping();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "topping":
                            try {
                                if (foodInfo.getString("temp").equals("true")) {
                                    chooseTemp();
                                } else {
                                    if (foodInfo.getJSONArray("customId").length() == 1){
                                        foodInfo.put("iceType", "null");
                                        goQuantity();
                                    } else if (foodInfo.getJSONArray("customId").length() >= 2){
                                        customIce();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "temp":
                            try {
                                if (numberinfo.equals("1")) {
                                    foodInfo.put("temp", "ice");
                                    customIce();
                                } else {
                                    foodInfo.put("iceType", "null");
                                    if (foodInfo.getJSONArray("customId").length() > 2) {
                                        chooseSweet();
                                    }
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                            break;
                        case "iceAmount":
                            try {
                                if (foodInfo.getJSONArray("customId").length() > 2) {
                                    chooseSweet();
                                } else {
                                    goQuantity();
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                            break;
                        case "sweet":
                            goQuantity();
                            break;
                        case "quantity":
                            addMenu();
                            break;
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

    void findMenu() {

        boolean flag = false;
        menuName = menuName.replace(" ", "");

        for (int i = 0; i < allFoodArray.size(); i++) {
            try {
                foodInfo = new JSONObject(allFoodArray.get(i));
                String name = foodInfo.getString("name").replace(" ", "");
                if (name.equals(menuName)) {
                    type = "custom";
                    flag = true;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!flag) {
            getUserSpeak("선택하신 메뉴가 없습니다. 상단 버튼을 누르고 메뉴를 다시 말씀해 주세요.");
        } else {
            try {
                if(foodInfo.getJSONArray("size").length() < 2){
                    JSONArray jsonArray = foodInfo.getJSONArray("price");
                    String price = jsonArray.getString(0);
                    foodInfo.put("price", price);
                    foodInfo.put("size", "null");
                    if (foodInfo.getString("customId").equals("null")) {
                        goQuantity();
                    } else {
                        chooseTopping();
                    }
                } else{
                    chooseSize();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void chooseSize() {
        type = "size";
        try {
            JSONArray jsonArray = foodInfo.getJSONArray("size");
            ArrayList<String> sizeList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++){
                sizeList.add(jsonArray.getString(i));
            }

            textToSpeech.speak("사이즈는 다음과 같습니다.", TextToSpeech.QUEUE_ADD, null);
            for (int i = 0; i < sizeList.size(); i++){
                textToSpeech.speak((i + 1) + " 번 " + sizeList.get(i) + "사이즈", TextToSpeech.QUEUE_ADD, null);
            }
            getUserSpeak("상단 버튼을 누르고 사이즈를 선택해 주세요.");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void chooseTopping() {

        try {
            JSONArray jsonArray = foodInfo.getJSONArray("customId");
            customId = jsonArray.getString(0);
            Log.d("Chk", customId);

            type = "topping";
            customArray = getStringId();
            JSONObject object = new JSONObject(customArray);

            JSONArray Array = object.getJSONArray("type");
            ArrayList<String> toppingType = new ArrayList<>();
            for (int i = 0; i < Array.length(); i++) {
                toppingType.add(Array.getString(i));
            }

            Array = object.getJSONArray("price");
            ArrayList<String> toppingPrice = new ArrayList<>();
            for (int i = 0; i < Array.length(); i++) {
                toppingPrice.add(Array.getString(i));
            }

            int num = set * 5;

            if (num > toppingType.size()) {
                set = 0;
                num = set * 5;
            }

            textToSpeech.speak("토핑은", TextToSpeech.QUEUE_ADD, null);
            for (int i = 0; i < 5; i++) {
                if (num + i >= toppingType.size()) { //토핑 개수보다 커지거나 5개 이상이 카운트 되면 반복문 탈출
                    break;
                }
                textToSpeech.speak((i + 1) + "번" + toppingType.get(num + i) + "가격은" + toppingPrice.get(num + i) + "원", TextToSpeech.QUEUE_ADD, null);
            }
            textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
            getUserSpeak("상단 버튼을 누르고 추가하고 싶은 토핑 번호를 말해주시고, 원하는 토핑카테고리가 없을 경우 0번을 말해주세요.");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void chooseTemp() {
        try {
            JSONArray jsonArray = foodInfo.getJSONArray("customId");
            customId = jsonArray.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        type = "temp";
        getUserSpeak("상단 버튼을 누르고 아이스를 원하시면 1번, 핫을 원하시면 2번을 말해 주세요.");

    }

    void customIce() {
        try {
            JSONArray Array = foodInfo.getJSONArray("customId");
            customId = Array.getString(1);

            type = "iceAmount";
            customArray = getStringId(); //얼음량 custom의 json파일을 string형태로 customArray에 저장

            JSONObject object = new JSONObject(customArray);
            JSONArray jsonArray = object.getJSONArray("type");
            ArrayList<String> iceType = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                iceType.add(jsonArray.getString(i));
            }
            textToSpeech.speak("얼음량은", TextToSpeech.QUEUE_ADD, null);
            for (int i = 0; i < iceType.size(); i++) {
                textToSpeech.speak((i + 1) + "번" + iceType.get(i), TextToSpeech.QUEUE_ADD, null);
            }
            textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
            getUserSpeak("상단 버튼을 누르고 얼음량을 선택해 주세요.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void chooseSweet() {
        try {
            JSONArray jsonArray = foodInfo.getJSONArray("customId");
            customId = jsonArray.getString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        type = "sweet";
        customArray = getStringId();
        try {
            JSONObject object = new JSONObject(customArray);
            JSONArray jsonArray = object.getJSONArray("type");
            ArrayList<String> sweetType = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                sweetType.add(jsonArray.getString(i));
            }
            textToSpeech.speak("당도는", TextToSpeech.QUEUE_ADD, null);
            for (int i = 0; i < sweetType.size(); i++) {
                textToSpeech.speak((i + 1) + "번" + sweetType.get(i), TextToSpeech.QUEUE_ADD, null);
            }
            textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
            getUserSpeak("상단 버튼을 누르고 당도를 선택해 주세요.");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void goQuantity() {
        type = "quantity";
        getUserSpeak("상단 버튼을 누르고 수량을 얘기해 주세요. 단, 최대 수량은 5개 입니다.");
    }

    void addMenu() {
        try {
            type = "finalMenu";
            int price = foodInfo.getInt("price");
            price = price * foodInfo.getInt("quantity");

            String name = foodInfo.getString("name");
            Integer quantity = foodInfo.getInt("quantity");
            String size = "null";
            String temp = "null";
            ArrayList<String> custom = new ArrayList<>();

            if (!foodInfo.getString("size").equals("null")) {
                size = foodInfo.getString("size");
            }
            if (foodInfo.get("customId") != "null") {
                custom.add(0, foodInfo.getString("toppingType"));
                price += foodInfo.getInt("toppingPrice");
                if (foodInfo.getJSONArray("customId").length() >= 2){
                    temp = foodInfo.getString("temp");
                    if (!foodInfo.getString("temp").equals("hot")){
                        custom.add(foodInfo.getString("iceType"));
                    }
                    if (foodInfo.getJSONArray("customId").length() == 3){
                        custom.add(foodInfo.getString("sweetType"));

                    }
                }
            }


            CartList cart = new CartList(name, size, temp, custom, price, quantity);
            cartList.add(cart);

            textToSpeech.speak(foodInfo.getString("name") + " 메뉴를 " + foodInfo.getInt("quantity") + "개 주문하셨습니다.", TextToSpeech.QUEUE_ADD, null);
            Log.d("Chk", foodInfo.toString());

            getUserSpeak("상단 버튼을 누른 후 주문을 계속하고 싶으면 1번을 말하고 주문을 마치고 싶으면 0번을 말해주세요.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void goLastOrder() {
        Intent intent = new Intent(context, LastOrder.class);
        intent.putExtra("menuList", cartList);
        startActivity(intent);
    }


}
