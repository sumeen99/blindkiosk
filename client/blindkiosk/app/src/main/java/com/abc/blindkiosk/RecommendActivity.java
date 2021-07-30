package com.abc.blindkiosk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class RecommendActivity extends AppCompatActivity {

    Button button_skip;
    Button button_put;
    TextToSpeech textToSpeech;
    Context context;

    String storeId;
    ArrayList<CartList> menuList;
    ArrayList<String> menuId = new ArrayList<>();
    List<String> allFoodArray;
    JSONObject foodInfo;
    CartList cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        button_skip = findViewById(R.id.button_skip);
        button_put = findViewById(R.id.button_put);
        context = getApplicationContext();
        storeId = getIntent().getStringExtra("storeId");
        menuList = (ArrayList<CartList>) getIntent().getSerializableExtra("menuList");
        for (int i = 0; i < menuList.size(); i++){
            menuId.add(menuList.get(i).id);
        }

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.KOREAN);
                    recommend();
                }
            }
        });

    }

    void recommend() {

        String recommendedId = getRecommendId();

        if (recommendedId.equals("null")){
            Intent intent = new Intent(this, LastOrder.class);
            intent.putExtra("menuList", menuList);
            startActivity(intent);
        } else {
            textToSpeech.speak("고객님의 주문을 바탕으로 추천하는 메뉴는", TextToSpeech.QUEUE_ADD, null);
            allFoodArray = getListId();
            for (int i = 0; i < allFoodArray.size(); i++){
                try {
                    foodInfo = new JSONObject(allFoodArray.get(i));
                    if (foodInfo.getString("_id").equals(recommendedId)){
                        textToSpeech.speak(foodInfo.getString("name") + "입니다.", TextToSpeech.QUEUE_ADD, null);
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            textToSpeech.speak("추천 메뉴를 담고 싶으면 하단버튼을 누르고 담고 싶지 않으면 상단버튼을 눌러주세요.", TextToSpeech.QUEUE_ADD, null);
        }

        try {
            JSONArray jsonArray = foodInfo.getJSONArray("price");
            Integer price = jsonArray.getInt(0);
            String name = foodInfo.getString("name");
            String id = foodInfo.getString("_id");

            cart = new CartList(id, name, null, "false", null, price, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        button_put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuList.add(cart);

                Intent intent = new Intent(context, LastOrder.class);
                intent.putExtra("menuList", menuList);
                startActivity(intent);
            }
        });


        button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LastOrder.class);
                intent.putExtra("menuList", menuList);
                startActivity(intent);
            }
        });

    }

    String getRecommendId() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        Callable<String> task = new Callable<String>() {
            @Override
            public String call() {
                return RecommendAPI.recommendMenu(menuId);
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

}
