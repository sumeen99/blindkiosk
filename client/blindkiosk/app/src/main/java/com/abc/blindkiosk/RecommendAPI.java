package com.abc.blindkiosk;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecommendAPI {

    static String recommendMenu(ArrayList<String> nameArray){
        String menuId = null;
        try {
            String name = "http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/recommend?id=" + nameArray.get(0);
            for (int i = 1; i < nameArray.size(); i++){
                name.concat("&id=" + nameArray.get(i));
            }

            URL url = new URL(name);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            String line;
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }
            bf.close();

            Log.d("Chk", result);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");

            menuId = jsonObject1.getString("recommendId");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return menuId;
    }
}
