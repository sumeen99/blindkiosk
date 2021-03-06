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

    ArrayList<CartList> cartList = new ArrayList<CartList>();

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
        setContentView(R.layout.activity_choose_menu);

        btnSpeech = (Button) findViewById(R.id.button_speak);
        btnOK = (Button) findViewById(R.id.button_payment);
        answer = (TextView) findViewById(R.id.textView);
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
            textToSpeech.speak("?????? ???????????? ????????? ??? ???????????????. ???????????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak("?????? ?????? ???????????????", TextToSpeech.QUEUE_ADD, null);
        for (int i = set * 5; i < set * 5 + 5; i++) {
            if (i < categoryList.size()) {
                MenuInfo category = categoryList.get(i);
                textToSpeech.speak(i - set * 5 + 1 + "??? " + category.name, TextToSpeech.QUEUE_ADD, null);
                numberCnt = i - set * 5 + 1;
            }
        }

        textToSpeech.speak("?????????.", TextToSpeech.QUEUE_ADD, null);
        tellUserNumberGuide("?????? ?????? ????????????",categoryList.size());
        getUserSpeak();
    }

    void getCategoryName() {
        MenuInfo categoryInfo = categoryList.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1);
        Log.d("categoryName", categoryInfo.name);
        menuIdInfo.setCategoryId(categoryInfo.id);
        textToSpeech.speak(categoryInfo.name + "??????????????? ?????????????????????.", TextToSpeech.QUEUE_ADD, null);

        set = 0;
        textToSpeech.speak(categoryInfo.name + "?????? ?????? ???????????? ???????????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        step = Constants.SUBCATEGORY_CHOOSE_STEPS;
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
            textToSpeech.speak("?????? ???????????? ????????? ??? ???????????????. ???????????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak("?????? ?????? ???????????????", TextToSpeech.QUEUE_ADD, null);
        for (int i = set * 5; i < set * 5 + 5; i++) {
            if (i < subcategoryList.size()) {
                MenuInfo category = subcategoryList.get(i);
                textToSpeech.speak(i - set * 5 + 1 + "??? " + category.name, TextToSpeech.QUEUE_ADD, null);
                numberCnt = i - set * 5 + 1;
            }
        }

        textToSpeech.speak("?????????.", TextToSpeech.QUEUE_ADD, null);
        tellUserNumberGuide("?????? ?????? ????????????",subcategoryList.size());
        getUserSpeak();
    }

    void getSubcategoryName() {
        MenuInfo subcategoryInfo = subcategoryList.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1);
        Log.d("subcategoryName", subcategoryInfo.name);
        menuIdInfo.setSubcategoryId(subcategoryInfo.id);
        textToSpeech.speak(subcategoryInfo.name + "??????????????? ?????????????????????.", TextToSpeech.QUEUE_ADD, null);

        set = 0;
        textToSpeech.speak(subcategoryInfo.name + "?????? ???????????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        step = Constants.FOOD_CHOOSE_STEPS;
        chooseFood();
    }

    void setFoodList() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
        Callable<List<FoodInfo>> task = new Callable<List<FoodInfo>>() {
            @Override
            public List<FoodInfo> call() {
                Log.d("Chk", "??????????????????: " + menuIdInfo.subcategoryId);
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
            textToSpeech.speak("?????? ?????? ????????? ??? ???????????????. ???????????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak("?????????", TextToSpeech.QUEUE_ADD, null);
        for (int i = set * 5; i < set * 5 + 5; i++) {
            if (i < foodList.size()) {
                FoodInfo food = foodList.get(i);

                if (food.size == null) {
                    textToSpeech.speak(i - set * 5 + 1 + "??? " + food.name + " " + food.price + "???", TextToSpeech.QUEUE_ADD, null);
                } else {
                    textToSpeech.speak(i - set * 5 + 1 + "??? " + food.name + " " + food.size + "????????? " + food.price + "???", TextToSpeech.QUEUE_ADD, null);
                }
                numberCnt = i - set * 5 + 1;
            }
        }

        textToSpeech.speak("?????????.", TextToSpeech.QUEUE_ADD, null);

        tellUserNumberGuide("??????",foodList.size());
        getUserSpeak();
    }

    void getFoodName() {
        userFood = foodList.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1);
        Log.d("FoodName", userFood.name);
        textToSpeech.speak(userFood.name + "????????? ?????????????????????.", TextToSpeech.QUEUE_ADD, null);
        name = (userFood.name);    //??????????????? ?????? ?????? ??????
        id = (userFood.id);
        price = (Integer.parseInt(userFood.price));
        size = (userFood.size);
        customCartList = new ArrayList<String>();
        if (userFood.temp) {
            step = Constants.TEMP_CHOOSE_STEPS;
            chooseTemp();
        } else {
            temp = (null);
            checkCustom();
        }


    }

    void chooseTemp() {
        numberCnt = 2;
        textToSpeech.speak("?????? ????????? ???????????????. 1??? ice, 2??? hot ?????????. ????????? ????????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
        getUserSpeak();
    }

    void getTemp() {
        if (answer.getText().toString().equals("1")) {
            temp = "ice";
        } else if (answer.getText().toString().equals("2")) {
            temp = "hot";
        }
        checkCustom();
    }

    void checkCustom() {
        if (userFood.customId == null) {
            customList = (null);   //????????? ?????? ??????
            step = Constants.FOOD_QUANTITY_CHOOSE_STEPS;
            chooseFoodQuantity();
        } else {
            set = 0;
            step = Constants.CUSTOM_CHOOSE_STEPS;
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

    void getCustomName(boolean iceSelection) {
        if(iceSelection) {  //????????? ????????? ????????? ??????
            textToSpeech.speak(customInfo.type.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1) + " ???????????? ?????????????????????.", TextToSpeech.QUEUE_ADD, null);
            customCartList.add(customInfo.type.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1));

            if (customInfo.price != null) {
                price += Integer.valueOf(customInfo.price.get(Integer.parseInt(answer.getText().toString()) + set * 5 - 1));
            }
        }
        set = 0;
        customInfoOrder += 1;
        if (customInfoOrder == userFood.customId.size()) {
            textToSpeech.speak("?????? ???????????? ?????????????????????.", TextToSpeech.QUEUE_ADD, null);
            customList = ((ArrayList<String>) customCartList);//????????? ?????? ??????
            step = Constants.FOOD_QUANTITY_CHOOSE_STEPS;
            chooseFoodQuantity();
            return;
        }
        textToSpeech.speak("?????? ??????????????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        chooseCustom();

    }

    void chooseCustom() {
        setCustomInfo(userFood.customId.get(customInfoOrder));
        if (customInfo.name.contains("??????") && temp.equals("hot")) {
            getCustomName(false);
        }
        if (set * 5 > customInfo.type.size()) {
            set = 0;
            textToSpeech.speak("?????? ????????? ??? ???????????????. ???????????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        }
        textToSpeech.speak(customInfo.name + " ???????????????.", TextToSpeech.QUEUE_ADD, null);
        if (customInfo.price != null) {
            for (int i = set * 5; i < set * 5 + 5; i++) {
                if (i < customInfo.type.size()) {
                    textToSpeech.speak(i - set * 5 + 1 + "??? " + customInfo.type.get(i) + " " + customInfo.price.get(i) + "???", TextToSpeech.QUEUE_ADD, null);
                    numberCnt = i - set * 5 + 1;
                }
            }
        } else {
            for (int i = set * 5; i < set * 5 + 5; i++) {
                if (i < customInfo.type.size()) {
                    textToSpeech.speak(i - set * 5 + 1 + "??? " + customInfo.type.get(i), TextToSpeech.QUEUE_ADD, null);
                    numberCnt = i - set * 5 + 1;
                }
            }
        }

        textToSpeech.speak("?????????.", TextToSpeech.QUEUE_ADD, null);
        tellUserNumberGuide(customInfo.name,customInfo.type.size());
        getUserSpeak();

    }

    void chooseFoodQuantity() {
        numberCnt = 5;
        textToSpeech.speak("????????? ????????? ????????? ??????????????????. 1?????? 1??? ?????????.", TextToSpeech.QUEUE_ADD, null);
        getUserSpeak();
    }

    void payOrAdd() {
        textToSpeech.speak("??????????????? ????????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        numberCnt = 3;
        step = Constants.CASH_OR_ADD_STEPS;
        textToSpeech.speak("????????? ???????????? 1???, ?????? ?????????????????? ????????? ???????????? 2???, ?????? ?????????????????? ????????? ???????????? 3?????? ???????????????.", TextToSpeech.QUEUE_ADD, null);
        getUserSpeak();
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

    void getUserSpeak() {
        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.stop();
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(Choose_Menu_1.this); //
                mRecognizer.setRecognitionListener(listener);   //?????? ????????? ???????????? ????????? ??????
                mRecognizer.startListening(intent);
            }
        });
    }


    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(context, "??????????????? ???????????????.", Toast.LENGTH_SHORT).show();
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
                    message = "????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "??????????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "????????? ??????";
                    textToSpeech.speak("???????????? ????????? ????????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "???????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "????????? ????????????";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "?????? ??? ??????";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER??? ??????";
                    textToSpeech.speak("?????? ?????? ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "????????? ?????????";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "????????? ????????????";
                    textToSpeech.speak("????????? ????????? ?????????????????????. ?????? ?????? ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                    break;
                default:
                    message = "??? ??? ?????? ?????????";
                    break;
            }

            Toast.makeText(context, "????????? ?????????????????????. : " + message, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < matches.size(); i++) {
                answer.setText(matches.get(i));
            }
            if (answer.getText().toString().contains("?????? ??????")) {
                textToSpeech.speak("?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                switch (step) {
                    case Constants.CATEGORY_CHOOSE_STEPS:
                        chooseCategory();
                        break;
                    case Constants.SUBCATEGORY_CHOOSE_STEPS:
                        chooseSubcategory();
                        break;
                    case Constants.FOOD_CHOOSE_STEPS:
                        chooseFood();
                        break;
                    case Constants.CUSTOM_CHOOSE_STEPS:
                        textToSpeech.speak("????????? ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                        set += 1;
                        chooseCustom();
                        break;
                }
            } else {
                answerInfo = number.findNumberByCnt(answer.getText().toString(), numberCnt);
                if (answerInfo == null) {
                    textToSpeech.speak("????????? ???????????? ??????????????????. ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                } else {
                    textToSpeech.speak("???????????? ????????? " + answerInfo + "?????? ????????? ?????? ????????? ??????????????? ????????? ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textToSpeech.stop();
                            answer.setText(answerInfo);
                            if (Integer.parseInt(answer.getText().toString()) == 0) {
                                switch (step) {
                                    case Constants.CATEGORY_CHOOSE_STEPS:
                                        textToSpeech.speak("????????? ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                                        set += 1;
                                        chooseCategory();
                                        break;
                                    case Constants.SUBCATEGORY_CHOOSE_STEPS:
                                        textToSpeech.speak("????????? ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                                        set += 1;
                                        chooseSubcategory();
                                        break;
                                    case Constants.FOOD_CHOOSE_STEPS:
                                        textToSpeech.speak("????????? ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                                        set += 1;
                                        chooseFood();
                                        break;
                                    case Constants.CUSTOM_CHOOSE_STEPS:
                                        textToSpeech.speak("????????? ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
                                        set += 1;
                                        chooseCustom();
                                        break;
                                    default:
                                        textToSpeech.speak("????????? ????????? ???????????? ??????????????????. ?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
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
                                        getCustomName(true);
                                        break;
                                    case Constants.TEMP_CHOOSE_STEPS:
                                        getTemp();
                                        break;
                                    case Constants.FOOD_QUANTITY_CHOOSE_STEPS:
                                        quantity = (Integer.parseInt(answer.getText().toString()));    //??????????????? ?????? ??????
                                        payOrAdd();
                                        break;
                                    case Constants.CASH_OR_ADD_STEPS:
                                        getPayOrAddInfo();
                                        break;
                                }

                            }
                            Toast.makeText(context, "?????? ??????", Toast.LENGTH_SHORT).show();
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

    void tellUserNumberGuide(String type, int listSize){
        if(listSize<=Constants.TOTAL_SIZE){
            textToSpeech.speak("????????? " +type+" ????????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
        }else {
            textToSpeech.speak("????????? "+type+" ????????? ?????????????????? ????????? 0?????? ??????????????????.", TextToSpeech.QUEUE_ADD, null);
        }
    }
}