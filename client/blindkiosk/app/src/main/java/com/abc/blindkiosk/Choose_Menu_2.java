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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Choose_Menu_2 extends AppCompatActivity {

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

    List<String> arrayId;
    String customArray;
    JSONArray CategoryArray;
    JSONArray MenuArray;
    int set = 0;

    String type = "store"; // type = store, category, subcategory, food, food_plus, food_exp, food_name, temp, custom
    String beforeType = null;
    String storeName;
    String storeId;
    String categoryId;
    String subcategoryId;
    String material;
    String customId;
    JSONObject foodInfo; //카트에 넣을 메뉴 정보를 담는 json - (+iceType)

    ArrayList<CartList> cartList = new ArrayList<>();
    ArrayList<String> moveCategory_kor = new ArrayList<>(Arrays.asList("상위", "하위", "메뉴명"));


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
            ActivityCompat.requestPermissions(Choose_Menu_2.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 1);
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
                    Category();
                }
            }
        });

    }

    void Category(){

        type = "category";
        JSONArray jsonArray = new JSONArray();
        JSONObject listId;
        int num = set * 5;

        arrayId = getListId(storeId, type);
        textToSpeech.speak("상위 카테고리는", TextToSpeech.QUEUE_ADD, null);

        if (num > arrayId.size()){
            set = 0;
            num = set * 5;
        }
        for (int i = 0; i < 5; i++){
            if(num + i >= arrayId.size()){
                break;
            }
            try {
                listId = new JSONObject(arrayId.get(num + i));
                textToSpeech.speak((i + 1) + " 번 " + listId.getString("name"), TextToSpeech.QUEUE_ADD, null);
                jsonArray.put(listId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
        CategoryArray = jsonArray;

        getUserSpeak("카테고리 번호를 말씀해주시고 원하는 카테고리가 없으면 0번을 말씀해주세요.");

    }

    void SubCategory(){

        type = "subcategory";
        JSONArray jsonArray = new JSONArray();
        JSONObject listId;
        int num = set * 5;

        arrayId = getListId(categoryId, type);
        textToSpeech.speak("하위 카테고리는", TextToSpeech.QUEUE_ADD, null);

        if (num > arrayId.size()){
            set = 0;
            num = set * 5;
        }
        for (int i = 0; i < 5; i++){
            if(num + i >= arrayId.size()){
                break;
            }
            try {
                listId = new JSONObject(arrayId.get(num + i));
                textToSpeech.speak((i + 1) + " 번 " + listId.getString("name"), TextToSpeech.QUEUE_ADD, null);
                jsonArray.put(listId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
        CategoryArray = jsonArray;
        getUserSpeak("카테고리 번호를 말씀해주시고 원하는 카테고리가 없으면 0번을 말씀해주세요.");

    }

    void Food(){
        type = "food";

        arrayId = getListId(subcategoryId, type);

        getUserSpeak("원하는 재료가 있으면 1번, 제외시키고 싶은 재료가 있으면 2번을 말해주세요.");
    }

    void getUserSpeak(String guideText) {
        textToSpeech.speak(guideText, TextToSpeech.QUEUE_ADD, null);
        button_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.stop();
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(Choose_Menu_2.this);
                mRecognizer.setRecognitionListener(listener);   //모든 콜백을 수신하는 리스너 설정
                mRecognizer.startListening(intent);
            }
        });
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
                } else{
                    return MenuAPI.custom(customId);
                }
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

    List<String> getListId(final String id, final String type) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<List<String>> task = new Callable<List<String>>() {
            @Override
            public List<String> call() {
                switch (type) {
                    case "category":
                        return MenuAPI.category(id);
                    case "subcategory":
                        return MenuAPI.subcategory(id);
                    default:
                        return MenuAPI.food(id);
                }

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

            if(!type.equals("food_plus") && !type.equals("food_exp")) {
                numberinfo = number.findNumber(textView.getText().toString());
                if (numberinfo != null) {
                    if (numberinfo.equals("0")) { //리스트 5개 다시 불러주는 경우
                        set += 1;
                        switch (type) {
                            case "category":
                                Category();
                                break;
                            case "subcategory":
                                SubCategory();
                                break;
                            case "food_name":
                                type = beforeType;
                                goMenu();
                                break;
                            case "topping":
                                chooseTopping();
                                break;
                            case "finalMenu":
                                goLastOrder();
                        }
                    } else { //사용자 말한게 0이 아닌 다른경우
                        String name = null;
                        switch (type) {
                            case "food":
                                if (numberinfo.equals("1")) { //재료 포함하는 경우
                                    type = "food_plus"; //타입 변경
                                    getUserSpeak("원하는 재료를 말해 주세요.");
                                } else if (numberinfo.equals("2")) { //재료 제외하는 경우
                                    type = "food_exp";
                                    getUserSpeak("제외시키고 싶은 재료를 말해 주세요.");
                                }
                                break;
                            case "category":
                            case "subcategory":  //카테고리 선택
                                try {
                                    JSONObject jsonName = CategoryArray.getJSONObject(Integer.parseInt(numberinfo) - 1);
                                    name = jsonName.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                textToSpeech.speak(name + " 카테고리를 선택한 것이 맞으면 하단버튼을 아니면 번호를 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                break;
                            case "food_name":
                                try {
                                    foodInfo = MenuArray.getJSONObject(Integer.parseInt(numberinfo) - 1);
                                    name = foodInfo.getString("name");
                                    textToSpeech.speak(name + " 메뉴를 선택한 것이 맞으면 하단 버튼을 누르고 아니면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
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
                                    textToSpeech.speak(toppingType.get(set * 5 + Integer.parseInt(numberinfo) - 1) + " 토핑을 선택한 것이 맞으면 하단 버튼을 누르고 아니면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "temp":
                                if (numberinfo.equals("1")) {
                                    try {
                                        foodInfo.put("temp", "ice");
                                        textToSpeech.speak("아이스를 선택하셨습니다. 맞으면 하단 버튼을 누르고 아니면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (numberinfo.equals("2")) {
                                    try {
                                        foodInfo.put("temp", "hot");
                                        textToSpeech.speak("핫을 선택하셨습니다. 맞으면 하단 버튼을 누르고 아니면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    textToSpeech.speak("잘못된 대답입니다. 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
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
                                    textToSpeech.speak("맞으면 하단 버튼을 누르고 아니라면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
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
                                    textToSpeech.speak("맞으면 하단 버튼을 누르고 아니라면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "quantity":
                                try {
                                    foodInfo.put("quantity", Integer.parseInt(numberinfo));
                                    textToSpeech.speak(numberinfo + " 개 선택하셨습니다. 맞으면 하단 버튼을 누르고 아니라면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "finalMenu":
                                textToSpeech.speak(moveCategory_kor.get(Integer.parseInt(numberinfo) - 1) + " 카테고리를 선택하셨습니다. 맞으면 하단 버튼을 누르고 아니라면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                                break;
                        }
                    }
                } else {
                    textToSpeech.speak("잘못된 답변입니다. 다시 말씀해 주세요.", TextToSpeech.QUEUE_ADD, null);
                }
            } else if(type.equals("food_plus")){ //food_plus food_exp는 string값을 받음
                material = textView.getText().toString();
                textToSpeech.speak(material + " 재료를 원하는 것이 맞으면 하단 버튼을 누르고 아니라면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
            } else if(type.equals("food_exp")){
                material = textView.getText().toString();
                textToSpeech.speak(material + " 재료를 제외시키고 싶은 것이 맞으면 하단 버튼을 누르고 아니라면 다시 말해주세요.", TextToSpeech.QUEUE_ADD, null);
            }

            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textToSpeech.stop();
                    set = 0;
                    switch (type) {
                        case "category":
                        case "subcategory":
                            goCategory();
                            break;
                        case "food_plus":
                        case "food_exp":
                            goMenu();
                            break;
                        case "food_name":
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
                        case "finalMenu":
                            if(Integer.parseInt(numberinfo) == 1){
                                type = "category";
                                Category();
                            } else if(Integer.parseInt(numberinfo) == 2){
                                type = "subcategory";
                                SubCategory();
                            } else if(Integer.parseInt(numberinfo) == 3){
                                type = "food";
                                Food();
                            }
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

    void goCategory() {
        int num = Integer.parseInt(numberinfo) - 1;

        if(type.equals("category")){
            try {
                JSONObject jsonObject = CategoryArray.getJSONObject(num);
                categoryId = jsonObject.getString("_id");
                SubCategory();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(type.equals("subcategory")){
            try {
                JSONObject jsonObject = CategoryArray.getJSONObject(num);
                subcategoryId = jsonObject.getString("_id");
                Food();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            textToSpeech.speak("잘못된 대답입니다. 다시 대답해 주세요.", TextToSpeech.QUEUE_ADD, null);
        }

    }

    void goMenu(){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        int num = set * 5;
        if (num > arrayId.size()){
            set = 0;
            num = set * 5;
        }

        int i = 0; //반복문에서 사용
        int cnt = 0; //5개 카운트할 때 사용

        if(type.equals("food_plus")){
            beforeType = type;
            textToSpeech.speak(material + "재료가 들어간 메뉴는 다음과 같습니다.", TextToSpeech.QUEUE_ADD, null);
            while(cnt < 5){
                if(num + i >= arrayId.size()){ //메뉴 개수보다 커지거나 5개 이상이 카운트 되면 반복문 탈출
                    break;
                }
                try {
                    List<String> materials = new ArrayList<>();
                    jsonObject = new JSONObject(arrayId.get(num + i)); //jsonarray에서  num+i번째 메뉴 jsonobject로 추출
                    JSONArray jsonArray1 = jsonObject.getJSONArray("material");
                    for(int k = 0; k < jsonArray1.length(); k++){ //추출한 jsonobject에서 material정보 list로 추출
                        materials.add(jsonArray1.getString(k));
                    }
                    if(materials.contains(material)) { //재료를 포함하고 있는 경우
                        ArrayList<String> prices= new ArrayList<>();
                        jsonArray1 = jsonObject.getJSONArray("price");
                        for(int k = 0; k < jsonArray1.length(); k++){
                            prices.add(jsonArray1.getString(k));
                        }
                        if(jsonObject.get("size")=="null"){ //size가 따로 없으면 가격 바로 말하기
                            textToSpeech.speak((cnt + 1) + " 번 " + jsonObject.getString("name") + " " + prices.get(0) + " 원", TextToSpeech.QUEUE_ADD, null);
                            jsonArray.put(jsonObject); //jsonarray에 재료가 포함된 메뉴 put
                            cnt += 1;
                        } else{ //size가 있으면 size개수별로 나눠서 말하기
                            ArrayList<String> sizes= new ArrayList<>();
                            jsonArray1 = jsonObject.getJSONArray("size");
                            for(int k = 0; k < jsonArray1.length(); k++){
                                sizes.add(jsonArray1.getString(k));
                            }
                            for (int j = 0; j < sizes.size(); j++){ //사이즈 별로 jsonObject를 jsonArray에 추가
                                textToSpeech.speak((cnt + 1 + j) + " 번 " + jsonObject.getString("name") + " " + sizes.get(j) + " 사이즈 " + prices.get(j) + " 원", TextToSpeech.QUEUE_ADD, null);
                                JSONObject jsonObject1 = new JSONObject(arrayId.get(num + i));
                                jsonObject1.put("size", sizes.get(j));
                                jsonObject1.put("price", prices.get(j));
                                jsonArray.put(jsonObject1);
                                cnt += 1;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i += 1;
            }
            MenuArray = jsonArray;
            type = "food_name";
            getUserSpeak("원하는 메뉴의 번호를 말해주시고 원하는 메뉴가 없으면 0번을 말해주세요.");

        } else if(type.equals("food_exp")){
            beforeType = type;
            textToSpeech.speak(material + "재료가 포함되지 않은 메뉴는 다음과 같습니다.", TextToSpeech.QUEUE_ADD, null);
            while(cnt < 5){
                if(num + i >= arrayId.size()){ //메뉴 개수보다 커지거나 5개 이상이 카운트 되면 반복문 탈출
                    break;
                }
                try {
                    List<String> materials = new ArrayList<>();
                    jsonObject = new JSONObject(arrayId.get(num + i)); //jsonarray에서  num+i번째 메뉴 jsonobject로 추출
                    JSONArray jsonArray1 = jsonObject.getJSONArray("material");
                    for(int k = 0; k < jsonArray1.length(); k++){ //추출한 jsonobject에서 material정보 list로 추출
                        materials.add(jsonArray1.getString(k));
                    }
                    if(!materials.contains(material)) { //재료를 포함하지 않는 경우
                        ArrayList<String> prices= new ArrayList<>();
                        jsonArray1 = jsonObject.getJSONArray("price");
                        for(int k = 0; k < jsonArray1.length(); k++){
                            prices.add(jsonArray1.getString(k));
                        }
                        if(jsonObject.get("size")=="null"){ //size가 따로 없으면 가격 바로 말하기
                            textToSpeech.speak((cnt + 1) + " 번 " + jsonObject.getString("name") + " " + prices.get(0) + "원", TextToSpeech.QUEUE_ADD, null);
                            jsonArray.put(jsonObject); //jsonarray에 재료가 포함된 메뉴 put
                            cnt += 1;
                        } else{ //size가 있으면 2개로 나눠서 말하기
                            ArrayList<String> sizes= new ArrayList<>();
                            jsonArray1 = jsonObject.getJSONArray("size");
                            for(int k = 0; k < jsonArray1.length(); k++){
                                sizes.add(jsonArray1.getString(k));
                            }
                            for (int j = 0; j < sizes.size(); j++){ //사이즈 별로 jsonObject를 jsonArray에 추가
                                textToSpeech.speak((cnt + 1 + j) + " 번 " + jsonObject.getString("name") + " " + sizes.get(j) + " 사이즈 " + prices.get(j) + " 원", TextToSpeech.QUEUE_ADD, null);
                                JSONObject jsonObject1 = new JSONObject(arrayId.get(num + i));
                                jsonObject1.put("size", sizes.get(j));
                                jsonObject1.put("price", prices.get(j));
                                jsonArray.put(jsonObject1);
                                cnt += 1;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i += 1;
            }
            MenuArray = jsonArray;
            type = "food_name";
            getUserSpeak("원하는 메뉴의 번호를 말해주시고 원하는 메뉴가 없으면 0번을 말해주세요.");

        }
    }

    void chooseTopping(){

        try {
            JSONArray jsonArray = foodInfo.getJSONArray("customId");
            customId = jsonArray.getString(0);
            Log.d("Chk", customId);

            type = "topping";
            customArray = getStringId();
            JSONObject object = new JSONObject(customArray);

            JSONArray Array = object.getJSONArray("type");
            ArrayList<String> toppingType = new ArrayList<>();
            for(int i = 0; i < Array.length(); i++){
                toppingType.add(Array.getString(i));
            }

            Array = object.getJSONArray("price");
            ArrayList<String> toppingPrice = new ArrayList<>();
            for(int i = 0; i < Array.length(); i++){
                toppingPrice.add(Array.getString(i));
            }

            int num = set * 5;

            if(num > toppingType.size()){
                set = 0;
                num = set * 5;
            }

            textToSpeech.speak("토핑은", TextToSpeech.QUEUE_ADD, null);
            for(int i = 0; i < 5; i++) {
                if (num + i >= toppingType.size()) { //토핑 개수보다 커지거나 5개 이상이 카운트 되면 반복문 탈출
                    break;
                }
               textToSpeech.speak((i + 1) + "번" + toppingType.get(num + i) + "가격은" + toppingPrice.get(num + i) + "원", TextToSpeech.QUEUE_ADD, null);
            }
            textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
            getUserSpeak("추가하고 싶은 토핑 번호를 말해주시고, 원하는 토핑카테고리가 없을 경우 0번을 말해주세요.");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void chooseTemp(){
        try {
            JSONArray jsonArray = foodInfo.getJSONArray("customId");
            customId = jsonArray.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        type = "temp";
        getUserSpeak("아이스를 원하시면 1번, 핫을 원하시면 2번을 말해 주세요.");

    }

    void customIce(){
        try {
            JSONArray Array = foodInfo.getJSONArray("customId");
            customId = Array.getString(1);

            type = "iceAmount";
            customArray = getStringId(); //얼음량 custom의 json파일을 string형태로 customArray에 저장

            JSONObject object = new JSONObject(customArray);
            JSONArray jsonArray = object.getJSONArray("type");
            ArrayList<String> iceType = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++){
                iceType.add(jsonArray.getString(i));
            }
            textToSpeech.speak("얼음량은", TextToSpeech.QUEUE_ADD, null);
            for(int i = 0; i < iceType.size(); i++){
                textToSpeech.speak((i + 1) + "번" + iceType.get(i) ,TextToSpeech.QUEUE_ADD,null);
            }
            textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
            getUserSpeak("얼음량을 선택해 주세요.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void chooseSweet(){
        try {
            JSONArray jsonArray = foodInfo.getJSONArray("customId");
            customId = jsonArray.getString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        type = "sweet";
        customArray = getStringId();
        try {
            JSONObject object  = new JSONObject(customArray);
            JSONArray jsonArray = object.getJSONArray("type");
            ArrayList<String> sweetType = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++){
                sweetType.add(jsonArray.getString(i));
            }
            textToSpeech.speak("당도는", TextToSpeech.QUEUE_ADD, null);
            for(int i = 0; i < sweetType.size(); i++){
                textToSpeech.speak((i + 1) + "번" + sweetType.get(i) ,TextToSpeech.QUEUE_ADD,null);
            }
            textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
            getUserSpeak("당도를 선택해 주세요.");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void goQuantity(){
        type = "quantity";
        getUserSpeak("수량을 말해 주세요. 단, 최대 수량은 5개 입니다.");
    }

    void addMenu(){
        try {
            type = "finalMenu";
            int price = foodInfo.getInt("price");
            price = price * foodInfo.getInt("quantity");

            String id = foodInfo.getString("_id");
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

            CartList cart = new CartList(id, name, size, temp, custom, price, quantity);
            cartList.add(cart);

            textToSpeech.speak(foodInfo.getString("name")+" 메뉴를 " + foodInfo.getInt("quantity") + "개 주문하셨습니다.", TextToSpeech.QUEUE_ADD, null);
            Log.d("Chk", foodInfo.toString());

            textToSpeech.speak("돌아가실 수 있는 카테고리는 다음과 같습니다.", TextToSpeech.QUEUE_ADD, null);
            for(int i = 0; i < 3; i++){
                textToSpeech.speak((i + 1)+ "번" + moveCategory_kor.get(i) + "카테고리", TextToSpeech.QUEUE_ADD, null);
            }
            getUserSpeak("주문을 계속하고 싶으면 원하는 카테고리의 번호를 말하고 주문을 마치고 싶으면 0번을 말해주세요.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void goLastOrder(){
        Intent intent = new Intent(context, RecommendActivity.class);
        intent.putExtra("menuList", cartList);
        intent.putExtra("storeId", storeId);
        startActivity(intent);
    }
}