package com.abc.blindkiosk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuAPI {
    String accessMenu(String name){
        List<String> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/menu?name="+name);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line = "";
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject storeObject = jsonObject.getJSONObject("data");
            String storeId = storeObject.getString("_id");
            return storeId;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    String accessCategory(String id){
        List<String> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/category?id="+id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line = "";
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray categoryArray = (JSONArray) jsonObject.get("data");
            //String storeId = storeObject.getString("_id");
            //return storeId;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
