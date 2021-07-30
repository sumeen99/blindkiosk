package com.abc.blindkiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
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

public class Choose_Menu_1 extends AppCompatActivity {
    String storeName;
    Intent intent;
    TextToSpeech textToSpeech;
    Button btnSpeech;
    Button btnOK;
    TextView answer;
    TextView stepView;
    Context context;
    String answerInfo;
    Number number = new Number();
    SpeechRecognizer mRecognizer;
    MenuAPI menuAPI = new MenuAPI();

    MenuIdInfo menuIdInfo = new MenuIdInfo();
    List<MenuInfo> categoryList;
    List<MenuInfo> subcategoryList;
    List<FoodInfo> foodList;
    CustomInfo customInfo;

    List<String> customCartList;
    int step;
    int set = 0;
    int customInfoOrder = 0;
    int numberCnt;
    FoodInfo userFood;

    ArrayList<CartList> cartList;
    String id;
    String name;
    String size;
    String temp;
    ArrayList<String> customList;
    Integer price;
    Integer quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        btnSpeech = (Button) findViewById(R.id.btnSpeech);
        btnOK = (Button) findViewById(R.id.btnOK);
        answer = (TextView) findViewById(R.id.TextViewAnswer);
        stepView = (TextView) findViewById(R.id.TextViewLocation);
        context = getApplicationContext();
        intent = getIntent();
        storeName = intent.getStringExtra("storeName");
        setStoreId();
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.KOREAN);
                    step = Constants.CATEGORY_CHOOSE_STEPS;
                    stepView.setText("카테고리 선택");
                    chooseCategory();
                }
            }
        });
    }

    void setCategoryList() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<List<MenuInfo>> task = new Callable<List<MenuInfo>>() {
            @Override
            public List<MenuInfo> call() {
                MenuAPI menuAPI = new MenuAPI();
                return menuAPI.accessCategory(menuIdInfo.storeId);
            }
        };
        Future<List<MenuInfo>> future = executorService.submit(task);

        try {
            categoryList = future.get();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    void setStoreId() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<String> task = new Callable<String>() {
            @Override
            public String call() {
                MenuAPI menuAPI = new MenuAPI();
                return menuAPI.accessMenu(storeName);
            }
        };
        Future<String> future = executorService.submit(task);

        try {
            menuIdInfo.setStoreId(future.get());
            //Log.d("StoreID",menuIdInfo.storeId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    void chooseCategory() {
        if (categoryList == null) {
            setCategoryList();

        }

        if (set * 5 > categoryList.size()) {
            set = 0;
            textToSpeech.speak("모든 카테고리 정보를 다 보았습니다. 처음으로 돌아갑니다.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak("메뉴 카테고리는", TextToSpeech.QUEUE_ADD, null);
        for (int i = set * 5; i < set * 5 + 5; i++) {
            if (i < categoryList.size()) {
                MenuInfo category = categoryList.get(i);
                textToSpeech.speak(i - set * 5 + 1 + "번 " + category.name, TextToSpeech.QUEUE_ADD, null);
                numberCnt = i - set * 5 + 1;
            }
        }

        textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
        getUserSpeak("화면 상단을 눌러 카테고리 번호를 말씀해주시고 원하는 카테고리가 없으면 0번을 말씀해주세요.");
    }

    void getCategoryName() {
        MenuInfo categoryInfo = categoryList.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1);
        Log.d("categoryName", categoryInfo.name);
        menuIdInfo.setCategoryId(categoryInfo.id);
        textToSpeech.speak(categoryInfo.name + "카테고리를 선택하셨습니다.", TextToSpeech.QUEUE_ADD, null);

        set = 0;
        textToSpeech.speak(categoryInfo.name + "하위 메뉴 카테고리 선택으로 넘어갑니다.", TextToSpeech.QUEUE_ADD, null);
        step = Constants.SUBCATEGORY_CHOOSE_STEPS;
        stepView.setText("하위 카테고리 선택");
        chooseSubcategory();
    }

    void setSubcategoryList() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<List<MenuInfo>> task = new Callable<List<MenuInfo>>() {
            @Override
            public List<MenuInfo> call() {
                return menuAPI.accessSubcategory(menuIdInfo.categoryId);

            }
        };
        Future<List<MenuInfo>> future = executorService.submit(task);

        try {
            subcategoryList = future.get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    void chooseSubcategory() {
        if (subcategoryList == null) {
            setSubcategoryList();
        }
        if (set * 5 > subcategoryList.size()) {
            set = 0;
            textToSpeech.speak("모든 카테고리 정보를 다 보았습니다. 처음으로 돌아갑니다.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak("하위 메뉴 카테고리는", TextToSpeech.QUEUE_ADD, null);
        for (int i = set * 5; i < set * 5 + 5; i++) {
            if (i < subcategoryList.size()) {
                MenuInfo category = subcategoryList.get(i);
                textToSpeech.speak(i - set * 5 + 1 + "번 " + category.name, TextToSpeech.QUEUE_ADD, null);
                numberCnt = i - set * 5 + 1;
            }
        }

        textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
        getUserSpeak("화면 상단을 눌러 카테고리 번호를 말씀해주시고 원하는 카테고리가 없으면 0번을 말씀해주세요.");
    }

    void getSubcategoryName() {
        MenuInfo subcategoryInfo = subcategoryList.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1);
        Log.d("subcategoryName", subcategoryInfo.name);
        menuIdInfo.setSubcategoryId(subcategoryInfo.id);
        textToSpeech.speak(subcategoryInfo.name + "카테고리를 선택하셨습니다.", TextToSpeech.QUEUE_ADD, null);

        set = 0;
        textToSpeech.speak(subcategoryInfo.name + "음식 선택으로 넘어갑니다.", TextToSpeech.QUEUE_ADD, null);
        step = Constants.FOOD_CHOOSE_STEPS;
        stepView.setText("음식 선택");
        chooseFood();
    }

    void setFoodList() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
        Callable<List<FoodInfo>> task = new Callable<List<FoodInfo>>() {
            @Override
            public List<FoodInfo> call() {
                Log.d("Chk", "서브카테고리: " + menuIdInfo.subcategoryId);
                return menuAPI.accessFood(menuIdInfo.subcategoryId);

            }
        };
        Future<List<FoodInfo>> future = executorService.submit(task);

        try {
            foodList = future.get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    void chooseFood() {
        if (foodList == null) {
            setFoodList();
        }

        if (set * 5 > foodList.size()) {
            set = 0;
            textToSpeech.speak("모든 음식 정보를 다 보았습니다. 처음으로 돌아갑니다.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak("음식은", TextToSpeech.QUEUE_ADD, null);
        for (int i = set * 5; i < set * 5 + 5; i++) {
            if (i < foodList.size()) {
                FoodInfo food = foodList.get(i);

                if(food.size==null) {
                    textToSpeech.speak(i - set * 5 + 1 + "번 " + food.name + " " + food.price + "원", TextToSpeech.QUEUE_ADD, null);
                }else {
                    textToSpeech.speak(i - set * 5 + 1 + "번 " + food.name + " " + food.size + "사이즈 " + food.price + "원", TextToSpeech.QUEUE_ADD, null);
                }
                numberCnt = i - set * 5 + 1;
            }
        }

        textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
        getUserSpeak("화면 상단을 눌러 음식 번호를 말씀해주시고 원하는 음식이 없으면 0번을 말씀해주세요.");
    }

    void getFoodName() {
        userFood = foodList.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1);
        Log.d("FoodName", userFood.name);
        textToSpeech.speak(userFood.name + "음식을 선택하셨습니다.", TextToSpeech.QUEUE_ADD, null);
        name = (userFood.name);    //장바구니에 음식 이름 저장
        id = (userFood.id);
        price = (Integer.parseInt(userFood.price));
        size = (userFood.size);
        customCartList = new ArrayList<String>();
        if (userFood.temp) {
            step = Constants.TEMP_CHOOSE_STEPS;
            stepView.setText("ice/hot 선택");
            chooseTemp();
        } else {
            temp = (null);
            checkCustom();
        }


    }

    void chooseTemp() {
        numberCnt = 2;
        getUserSpeak("음료 종류를 선택합니다. 1번 ice, 2번 hot 입니다. 화면을 눌러 원하는 번호를 말씀해주세요.");
    }

    void getTemp() {
        if (answer.getText().toString().equals("1")) {
            temp = ("ice");
        } else if (answer.getText().toString().equals("2")) {
            temp = ("hot");
        }
        checkCustom();
    }

    void checkCustom() {
        if (userFood.customId == null) {
            customList = (null);   //커스텀 정보 없음
            step = Constants.FOOD_QUANTITY_CHOOSE_STEPS;
            stepView.setText("음식 수량 선택");
            chooseFoodQuantity();
        } else {
            set = 0;
            step = Constants.CUSTOM_CHOOSE_STEPS;
            stepView.setText("옵션 선택");
            chooseCustom();
        }
    }

    void setCustomInfo(final String customId) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<CustomInfo> task = new Callable<CustomInfo>() {
            @Override
            public CustomInfo call() {
                return menuAPI.accessCustom(customId);

            }
        };
        Future<CustomInfo> future = executorService.submit(task);

        try {
            customInfo = future.get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    void getCustomName() {
        if (customInfoOrder == userFood.customId.size() - 1) {
            textToSpeech.speak("모든 커스텀을 선택하셨습니다.", TextToSpeech.QUEUE_ADD, null);
            customList = ((ArrayList<String>) customCartList);//커스텀 정보 담기
            step = Constants.FOOD_QUANTITY_CHOOSE_STEPS;
            stepView.setText("음식 수량 선택");
            chooseFoodQuantity();
        } else {
            textToSpeech.speak(customInfo.type.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1) + " 커스텀을 선택하셨습니다.", TextToSpeech.QUEUE_ADD, null);
            customCartList.add(customInfo.type.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1));

            if (customInfo.price != null) {
                price += Integer.valueOf(customInfo.price.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1));
            }
            set = 0;
            customInfoOrder += 1;

            textToSpeech.speak("다음 커스텀으로 넘어갑니다.", TextToSpeech.QUEUE_ADD, null);
            chooseCustom();
        }
    }

    void chooseCustom() {
        setCustomInfo(userFood.customId.get(customInfoOrder));
        if (set * 5 > customInfo.type.size()) {
            set = 0;
            textToSpeech.speak("모든 정보를 다 보았습니다. 처음으로 돌아갑니다.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak(customInfo.name + " 부분입니다.", TextToSpeech.QUEUE_ADD, null);
        if (customInfo.price != null) {
            for (int i = set * 5; i < set * 5 + 5; i++) {
                if (i < customInfo.type.size()) {
                    textToSpeech.speak(i - set * 5 + 1 + "번 " + customInfo.type.get(i) + " " + customInfo.price.get(i) + "원", TextToSpeech.QUEUE_ADD, null);
                    numberCnt = i - set * 5 + 1;
                }
            }
        } else {
            for (int i = set * 5; i < set * 5 + 5; i++) {
                if (i < customInfo.type.size()) {
                    textToSpeech.speak(i - set * 5 + 1 + "번 " + customInfo.type.get(i), TextToSpeech.QUEUE_ADD, null);
                    numberCnt = i - set * 5 + 1;
                }
            }
        }

        textToSpeech.speak("입니다.", TextToSpeech.QUEUE_ADD, null);
        getUserSpeak("화면 상단을 눌러 " + customInfo.name + " 번호를 말씀해주시고 원하는 " + customInfo.name + " 번호가 없으면 0번을 말씀해주세요.");

    }

    void chooseFoodQuantity() {
        numberCnt = 5;
        getUserSpeak("주문할 개수를 번호로 말씀해주세요. 1번은 1개 입니다.");
    }

    void payOrAdd() {
        textToSpeech.speak("장바구니에 음식을 담았습니다.", TextToSpeech.QUEUE_ADD, null);
        numberCnt = 3;
        step = Constants.CASH_OR_ADD_STEPS;
        stepView.setText("결제/추가주문 선택");
        getUserSpeak("결제를 원하시면 1번, 하위 카테고리에서 주문을 원하시면 2번, 상위 카테고리에서 주문을 원하시면 3번을 눌러주세요.");


    }

    void getPayOrAddInfo() {
        switch (Integer.parseInt(answer.getText().toString())) {
            case 1:
                pay();
                break;
            case 2:
                foodList = null;
                customCartList = null;
                userFood = null;
                customInfo = null;
                set = 0;
                customInfoOrder = 0;
                step = Constants.FOOD_CHOOSE_STEPS;
                stepView.setText("음식 선택");
                chooseFood();
                break;
            case 3:
                subcategoryList = null;
                foodList = null;
                customCartList = null;
                userFood = null;
                customInfo = null;
                set = 0;
                customInfoOrder = 0;
                step = Constants.SUBCATEGORY_CHOOSE_STEPS;
                stepView.setText("하위 카테고리 선택");
                chooseSubcategory();
                break;
        }
    }

    void pay() {
        CartList cart = new CartList(id, name, size, temp, customList, price, quantity);
        cartList.add(cart);

        Intent cartIntent = new Intent(context, RecommendActivity.class);
        cartIntent.putExtra("menuList", cartList);
        cartIntent.putExtra("storeId", menuIdInfo.storeId);
        startActivity(cartIntent);
    }

    void getUserSpeak(String guideText) {
        textToSpeech.speak(guideText, TextToSpeech.QUEUE_ADD, null);
        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(Choose_Menu_1.this); //
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

            answerInfo = number.findNumberByCnt(answer.getText().toString(), numberCnt);

            if (answerInfo == null) {
                textToSpeech.speak("번호를 인식하지 못하였습니다. 화면 상단을 눌러 번호를 다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
            } else {
                textToSpeech.speak("선택하신 번호가 " + answerInfo + "번이 맞으면 화면 하단을 눌러주시고 아니면 화면 상단을 눌러 다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answer.setText(answerInfo);
                        if (Integer.parseInt(answer.getText().toString()) == 0) {
                            switch (step) {
                                case Constants.CATEGORY_CHOOSE_STEPS:
                                    textToSpeech.speak("번호를 새로 알려드립니다.", TextToSpeech.QUEUE_ADD, null);
                                    set += 1;
                                    chooseCategory();
                                    break;
                                case Constants.SUBCATEGORY_CHOOSE_STEPS:
                                    textToSpeech.speak("번호를 새로 알려드립니다.", TextToSpeech.QUEUE_ADD, null);
                                    set += 1;
                                    chooseSubcategory();
                                    break;
                                case Constants.FOOD_CHOOSE_STEPS:
                                    textToSpeech.speak("번호를 새로 알려드립니다.", TextToSpeech.QUEUE_ADD, null);
                                    set += 1;
                                    chooseFood();
                                    break;
                                case Constants.CUSTOM_CHOOSE_STEPS:
                                    textToSpeech.speak("번호를 새로 알려드립니다.", TextToSpeech.QUEUE_ADD, null);
                                    set += 1;
                                    chooseCustom();
                                    break;
                                default:
                                    textToSpeech.speak("올바른 번호를 인식하지 못하였습니다. 화면 상단을 눌러 다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
                                    break;
                            }


                        } else {
                            switch (step) {
                                case Constants.CATEGORY_CHOOSE_STEPS:
                                    getCategoryName();
                                    break;
                                case Constants.SUBCATEGORY_CHOOSE_STEPS:
                                    getSubcategoryName();
                                    break;
                                case Constants.FOOD_CHOOSE_STEPS:
                                    getFoodName();
                                    break;
                                case Constants.CUSTOM_CHOOSE_STEPS:
                                    getCustomName();
                                    break;
                                case Constants.TEMP_CHOOSE_STEPS:
                                    getTemp();
                                    break;
                                case Constants.FOOD_QUANTITY_CHOOSE_STEPS:
                                    quantity = (Integer.parseInt(answer.getText().toString()));    //장바구니에 수량 저장
                                    payOrAdd();
                                    break;
                                case Constants.CASH_OR_ADD_STEPS:
                                    getPayOrAddInfo();
                                    break;
                            }

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


}